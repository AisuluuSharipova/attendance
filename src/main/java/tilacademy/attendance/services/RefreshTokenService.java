package tilacademy.attendance.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tilacademy.attendance.entities.RefreshToken;
import tilacademy.attendance.entities.User;
import tilacademy.attendance.repositories.RefreshTokenRepository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    @Value("${app.refresh-token-duration-ms}")
    private Long refreshTokenDurationMs;


    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshToken createRefreshToken(User user) {
        RefreshToken rt = new RefreshToken();
        rt.setUser(user);
        rt.setExpiryDate(Instant.now().plusMillis(refreshTokenDurationMs));
        rt.setToken(UUID.randomUUID().toString() + "-" + UUID.randomUUID().toString());
        return refreshTokenRepository.save(rt);
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }
        return token;
    }

    @Transactional
    public int deleteByUserId(User user) {
        return refreshTokenRepository.deleteByUser(user);
    }
}
