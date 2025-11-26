package tilacademy.attendance.services;

import tilacademy.attendance.entities.Teacher;

import java.util.List;
import java.util.Optional;

public interface TeacherService {
    List<Teacher> findAll();
    Optional<Teacher> findById(Long id);
    Teacher create(Teacher teacher);
    Teacher update(Long id, Teacher teacher);
    void delete(Long id);
}
