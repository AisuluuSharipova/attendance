package tilacademy.attendance.dto.auth;

public record MeResponse(Long id, String username, String role, boolean enabled) {}
