package tilacademy.attendance.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import tilacademy.attendance.entities.RefreshToken;
import tilacademy.attendance.entities.User;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);

    int deleteByUser(User user);

    @Modifying
    @Transactional
    @Query("delete from RefreshToken rt where rt.expiryDate < :now")
    int deleteByExpiryDateBefore(Instant now);

    List<RefreshToken> findAllByUser(User user);
}
