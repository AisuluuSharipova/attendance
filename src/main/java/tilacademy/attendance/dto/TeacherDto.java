package tilacademy.attendance.dto;

public record TeacherDto(
        Long id,
        String firstName,
        String lastName,
        String fullName,
        String email,
        String phone
) {}
