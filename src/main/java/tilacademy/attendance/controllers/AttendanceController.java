package tilacademy.attendance.controllers;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tilacademy.attendance.dto.AttendanceDto;
import tilacademy.attendance.dto.CreateAttendanceDto;
import tilacademy.attendance.entities.Attendance;
import tilacademy.attendance.entities.AttendanceStatus;
import tilacademy.attendance.entities.Student;
import tilacademy.attendance.entities.Teacher;
import tilacademy.attendance.mappers.AttendanceMapper;
import tilacademy.attendance.services.AttendanceService;
import tilacademy.attendance.services.StudentService;
import tilacademy.attendance.services.TeacherService;

import java.time.LocalDate;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;
    private final AttendanceMapper attendanceMapper;
    private final StudentService studentService;
    private final TeacherService teacherService;

    public AttendanceController(AttendanceService attendanceService,
                                AttendanceMapper attendanceMapper,
                                StudentService studentService,
                                TeacherService teacherService) {
        this.attendanceService = attendanceService;
        this.attendanceMapper = attendanceMapper;
        this.studentService = studentService;
        this.teacherService = teacherService;
    }

    @PostMapping
    public ResponseEntity<AttendanceDto> create(@Valid @RequestBody CreateAttendanceDto dto) {
        Attendance a = attendanceMapper.toEntity(dto);

        Student student = studentService.findById(dto.studentId())
                .orElseThrow(() -> new IllegalArgumentException("Student not found: " + dto.studentId()));
        a.setStudent(student);

        if (dto.teacherId() != null) {
            Teacher teacher = teacherService.findById(dto.teacherId())
                    .orElseThrow(() -> new IllegalArgumentException("Teacher not found: " + dto.teacherId()));
            a.setTeacher(teacher);
        } else {
            a.setTeacher(null);
        }

        a.setStatus(AttendanceStatus.valueOf(dto.status()));

        Attendance created = attendanceService.create(a);
        return ResponseEntity.created(URI.create("/api/v1/attendance/" + created.getId()))
                .body(attendanceMapper.toDto(created));
    }

    @GetMapping("/date/{date}")
    public List<AttendanceDto> byDate(@PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        return attendanceService.findByDate(date).stream().map(attendanceMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/student/{studentId}")
    public List<AttendanceDto> byStudent(@PathVariable Long studentId) {
        return attendanceService.findByStudent(studentId).stream().map(attendanceMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/teacher/{teacherId}")
    public List<AttendanceDto> byTeacher(@PathVariable Long teacherId) {
        return attendanceService.findByTeacher(teacherId).stream().map(attendanceMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceDto> get(@PathVariable Long id) {
        return attendanceService.findById(id)
                .map(attendanceMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
