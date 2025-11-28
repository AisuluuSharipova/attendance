package tilacademy.attendance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import tilacademy.attendance.entities.RefreshToken;
import tilacademy.attendance.entities.User;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    int deleteByUser(User user);
}
