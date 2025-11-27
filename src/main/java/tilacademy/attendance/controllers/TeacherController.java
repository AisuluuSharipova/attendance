package tilacademy.attendance.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tilacademy.attendance.dto.*;
import tilacademy.attendance.entities.Teacher;
import tilacademy.attendance.mappers.TeacherMapper;
import tilacademy.attendance.services.TeacherService;
import jakarta.validation.Valid;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;

    public TeacherController(TeacherService teacherService, TeacherMapper teacherMapper) {
        this.teacherService = teacherService;
        this.teacherMapper = teacherMapper;
    }

    @GetMapping
    public List<TeacherDto> list() {
        return teacherService.findAll().stream().map(teacherMapper::toDto).collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherDto> get(@PathVariable Long id) {
        return teacherService.findById(id)
                .map(teacherMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<TeacherDto> create(@Valid @RequestBody CreateTeacherDto dto) {
        Teacher t = teacherMapper.toEntity(dto);
        Teacher created = teacherService.create(t);
        return ResponseEntity.created(URI.create("/api/v1/teachers/" + created.getId()))
                .body(teacherMapper.toDto(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeacherDto> update(@PathVariable Long id, @Valid @RequestBody UpdateTeacherDto dto) {
        Teacher t = teacherMapper.toEntity(dto);
        Teacher updated = teacherService.update(id, t);
        return ResponseEntity.ok(teacherMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teacherService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
