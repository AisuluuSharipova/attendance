package tilacademy.attendance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tilacademy.attendance.entities.Teacher;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    Optional<Teacher> findByEmail(String email);
}
