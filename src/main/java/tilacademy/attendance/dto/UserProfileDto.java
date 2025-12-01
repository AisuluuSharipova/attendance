package tilacademy.attendance.dto;

public record UserProfileDto(
        Long id,
        String username,
        String role,
        Boolean enabled,
        Long teacherId,
        String teacherName
) {}
