package tilacademy.attendance.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tilacademy.attendance.dto.*;
import tilacademy.attendance.entities.Pkg;
import tilacademy.attendance.entities.Student;
import tilacademy.attendance.mappers.StudentMapper;
import tilacademy.attendance.mappers.PkgMapper;
import tilacademy.attendance.services.PkgService;
import tilacademy.attendance.services.StudentService;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/students")
public class StudentController {

    private final StudentService studentService;
    private final PkgService pkgService;
    private final StudentMapper studentMapper;

    public StudentController(StudentService studentService, PkgService pkgService, StudentMapper studentMapper) {
        this.studentService = studentService;
        this.pkgService = pkgService;
        this.studentMapper = studentMapper;
    }

    @GetMapping
    public List<StudentListDto> list() {
        return studentService.findAll().stream().map(studentMapper::toListDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDetailDto> get(@PathVariable Long id) {
        return studentService.findById(id)
                .map(studentMapper::toDetailDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StudentDetailDto> create(@Valid @RequestBody CreateStudentDto dto) {
        Student s = studentMapper.toEntity(dto);

        if (dto.pkgId() != null) {
            Pkg pkg = pkgService.findById(dto.pkgId()).orElse(null);
            if (pkg == null) return ResponseEntity.badRequest().build();
            s.setPkg(pkg);
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
            if (pkg == null) return ResponseEntity.badRequest().build();
            s.setPkg(pkg);
        } else {
            s.setPkg(null);
        }

        Student updated = studentService.update(id, s);
        return ResponseEntity.ok(studentMapper.toDetailDto(updated));
    }

    /** ручное списание одного урока c кнопки */
    @PostMapping("/{id}/consume")
    public ResponseEntity<StudentDetailDto> consumeOne(@PathVariable Long id) {
        Student updated = studentService.consumeLesson(id);
        return ResponseEntity.ok(studentMapper.toDetailDto(updated));
    }
}
