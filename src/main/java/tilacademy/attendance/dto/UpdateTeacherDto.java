package tilacademy.attendance.dto;

import jakarta.validation.constraints.*;

public record UpdateTeacherDto(
        @NotBlank(message = "firstName is required")
        @Size(max = 100)
        String firstName,

        @NotBlank(message = "lastName is required")
        @Size(max = 100)
        String lastName,

        @Email(message = "email must be valid")
        String email,

        @Size(max = 50)
        String phone
) {}
