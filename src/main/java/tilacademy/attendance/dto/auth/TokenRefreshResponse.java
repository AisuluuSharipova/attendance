package tilacademy.attendance.dto.auth;

public record TokenRefreshResponse(String accessToken, String refreshToken) {
}
