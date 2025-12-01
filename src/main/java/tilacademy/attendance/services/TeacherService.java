package tilacademy.attendance.services;

import tilacademy.attendance.entities.Teacher;
import tilacademy.attendance.entities.User;

import java.util.List;
import java.util.Optional;

public interface TeacherService {
    List<Teacher> findAll();
    Optional<Teacher> findById(Long id);
    Teacher createTeacherForUser(User user, String firstName, String lastName, String email, String phone);
    Teacher update(Long id, Teacher teacher);
    void delete(Long id);
}
