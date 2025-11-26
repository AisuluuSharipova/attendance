package tilacademy.attendance.services;

import tilacademy.attendance.entities.Student;

import java.util.List;
import java.util.Optional;

public interface StudentService {
    List<Student> findAll();
    Optional<Student> findById(Long id);
    Student create(Student student);
    Student update(Long id, Student student);
    void delete(Long id);

    /**
     * Уменьшает lessonsRemaining на 1 для заданного студента.
     * Возвращает обновлённого студента.
     * По соглашению, negative values allowed.
     */
    Student consumeLesson(Long studentId);

    /**
     * Массовое списание урока для списка студентов.
     */
    void bulkConsume(List<Long> studentIds);
}
