package tilacademy.attendance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tilacademy.attendance.entities.Student;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {


    List<Student> findByTeacherId(Long teacherId);
    // For search within a teacher:
    List<Student> findByTeacherIdAndFirstNameContainingIgnoreCaseOrTeacherIdAndLastNameContainingIgnoreCase(Long teacherId1, String firstName, Long teacherId2, String lastName);
}
