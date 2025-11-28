package tilacademy.attendance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tilacademy.attendance.entities.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}
