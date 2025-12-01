package tilacademy.attendance.dto;

/**
 * Response returned when manager creates a teacher together with user credentials.
 * `rawPassword` is returned only at creation moment so manager can pass it to the teacher.
 */
public record TeacherWithCredentialsDto(
        Long id,
        String firstName,
        String lastName,
        String fullName,
        String email,
        String phone,
        String username,
        String rawPassword
) {}
