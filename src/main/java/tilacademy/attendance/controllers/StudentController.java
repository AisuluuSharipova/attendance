package tilacademy.attendance.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import tilacademy.attendance.dto.*;
import tilacademy.attendance.entities.Pkg;
import tilacademy.attendance.entities.Student;
import tilacademy.attendance.entities.Teacher;
import tilacademy.attendance.mappers.StudentMapper;
import tilacademy.attendance.services.PkgService;
import tilacademy.attendance.services.StudentService;
import tilacademy.attendance.repositories.UserRepository;
import tilacademy.attendance.repositories.TeacherRepository;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;
    private final PkgService pkgService;
    private final StudentMapper studentMapper;
    private final UserRepository userRepository;
    private final TeacherRepository teacherRepository;

    public StudentController(StudentService studentService,
                             PkgService pkgService,
                             StudentMapper studentMapper,
                             UserRepository userRepository,
                             TeacherRepository teacherRepository) {
        this.studentService = studentService;
        this.pkgService = pkgService;
        this.studentMapper = studentMapper;
        this.userRepository = userRepository;
        this.teacherRepository = teacherRepository;
    }

    /**
     * List students.
     * - If the caller has ROLE_TEACHER, returns only students of that teacher (determined by user -> teacher.userId).
     * - Managers/Admins may pass optional ?teacherId= to filter by teacher, otherwise get all.
     */
    @GetMapping
    public List<StudentListDto> list(@RequestParam(name = "teacherId", required = false) Long teacherId,
                                     Authentication authentication) {

        List<Student> students;

        // If the caller is a teacher, restrict to that teacher's students regardless of incoming teacherId
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> auths = authentication.getAuthorities();
            boolean isTeacher = auths != null && auths.stream()
                    .anyMatch(a -> "ROLE_TEACHER".equals(a.getAuthority()));

            if (isTeacher) {
                String username = authentication.getName();
                if (username != null) {
                    Optional.ofNullable(username)
                            .flatMap(userRepository::findByUsername)
                            .ifPresentOrElse(user -> {
                                Optional<Teacher> tOpt = teacherRepository.findByUser_Id(user.getId());
                                if (tOpt.isPresent()) {
                                    // short-circuit return by throwing a small runtime exception handled below,
                                    // or simply return the mapped result directly — here we'll set students via side-effect.
                                }
                            }, () -> {
                                // no user found, nothing to do
                            });

                    // find teacher & return immediately if present
                    Optional.ofNullable(username)
                            .flatMap(userRepository::findByUsername)
                            .flatMap(u -> teacherRepository.findByUser_Id(u.getId()))
                            .ifPresent(t -> {
                                List<StudentListDto> out = studentService.findByTeacherId(t.getId())
                                        .stream().map(studentMapper::toListDto).collect(Collectors.toList());
                                throw new EarlyReturnListException(out);
                            });

                    // if we reach here then teacher wasn't found -> return empty list
                    // (handled below)
                }
            }
        }

        // Non-teacher flow: honor optional filter or return all
        if (teacherId != null) {
            students = studentService.findByTeacherId(teacherId);
        } else {
            students = studentService.findAll();
        }

        return students.stream().map(studentMapper::toListDto).collect(Collectors.toList());
    }

    /**
     * Get single student.
     * NOTE: consider adding access check so teacher can only read their own student.
     */
    @GetMapping("/{id}")
    public ResponseEntity<StudentDetailDto> get(@PathVariable Long id, Authentication authentication) {
        Optional<Student> sOpt = studentService.findById(id);
        if (sOpt.isEmpty()) return ResponseEntity.notFound().build();

        Student s = sOpt.get();

        // If caller is a teacher, ensure student belongs to them
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> auths = authentication.getAuthorities();
            boolean isTeacher = auths != null && auths.stream()
                    .anyMatch(a -> "ROLE_TEACHER".equals(a.getAuthority()));
            if (isTeacher) {
                String username = authentication.getName();
                Optional.ofNullable(username).flatMap(userRepository::findByUsername)
                        .flatMap(u -> teacherRepository.findByUser_Id(u.getId()))
                        .ifPresentOrElse(t -> {
                            if (s.getTeacher() == null || !t.getId().equals(s.getTeacher().getId())) {
                                throw new ForbiddenAccessException();
                            }
                        }, () -> {
                            throw new ForbiddenAccessException();
                        });
            }
        }

        return ResponseEntity.ok(studentMapper.toDetailDto(s));
    }

    @PostMapping
    public ResponseEntity<StudentDetailDto> create(@Valid @RequestBody CreateStudentDto dto,
                                                   Authentication authentication) {
        Student s = studentMapper.toEntity(dto);

        if (dto.pkgId() != null) {
            Pkg pkg = pkgService.findById(dto.pkgId()).orElse(null);
            s.setPkg(pkg);
        }

        // If caller is a teacher, automatically link student to that teacher (MVP convenience)
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> auths = authentication.getAuthorities();
            boolean isTeacher = auths != null && auths.stream()
                    .anyMatch(a -> "ROLE_TEACHER".equals(a.getAuthority()));
            if (isTeacher) {
                String username = authentication.getName();
                Optional.ofNullable(username)
                        .flatMap(userRepository::findByUsername)
                        .flatMap(u -> teacherRepository.findByUser_Id(u.getId()))
                        .ifPresent(t -> s.setTeacher(t));
            } else {
                if (dto.teacherId() != null) {
                    Optional<Teacher> tOpt = teacherRepository.findById(dto.teacherId());
                    tOpt.ifPresent(s::setTeacher);
                }
            }
        }

        if (s.getLessonsRemaining() == null) s.setLessonsRemaining(0);

        Student created = studentService.create(s);
        StudentDetailDto out = studentMapper.toDetailDto(created);
        return ResponseEntity.created(URI.create("/api/v1/students/" + created.getId())).body(out);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDetailDto> update(@PathVariable Long id, @Valid @RequestBody UpdateStudentDto dto) {
        Student s = studentMapper.toEntity(dto);

        if (dto.pkgId() != null) {
            Pkg pkg = pkgService.findById(dto.pkgId()).orElse(null);
            s.setPkg(pkg);
        }

        // If DTO contains teacherId, ensure valid teacher (only managers/admins should be allowed to change teacher)
        if (dto.teacherId() != null) {
            Optional<Teacher> t = teacherRepository.findById(dto.teacherId());
            t.ifPresent(s::setTeacher);
        } else {
            s.setTeacher(null);
        }

        Student updated = studentService.update(id, s);
        return ResponseEntity.ok(studentMapper.toDetailDto(updated));
    }

    /** manual consume one lesson from student */
    @PostMapping("/{id}/consume")
    public ResponseEntity<StudentDetailDto> consumeOne(@PathVariable Long id) {
        Student updated = studentService.consumeLesson(id);
        return ResponseEntity.ok(studentMapper.toDetailDto(updated));
    }

    // Custom small exceptions used to control flow without changing method signatures.
    // Place these as static inner classes or move to a shared exceptions package as needed.

    // Thrown to carry precomputed list result for immediate return from list() when caller is a teacher.
    private static class EarlyReturnListException extends RuntimeException {
        private final List<StudentListDto> payload;
        public EarlyReturnListException(List<StudentListDto> payload) {
            this.payload = payload;
        }
        public List<StudentListDto> getPayload() { return payload; }
    }

    // Thrown when access must be forbidden (results in 403)
    private static class ForbiddenAccessException extends RuntimeException {}

    // Add a @ControllerAdvice elsewhere to map these exceptions:
    // - EarlyReturnListException -> return payload (200)
    // - ForbiddenAccessException -> ResponseEntity.status(403).build()
}
