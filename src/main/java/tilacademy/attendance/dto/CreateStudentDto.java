package tilacademy.attendance.dto;

import jakarta.validation.constraints.*;

public record CreateStudentDto(
        @NotBlank(message = "firstName is required")
        @Size(max = 100)
        String firstName,

        @NotBlank(message = "lastName is required")
        @Size(max = 100)
        String lastName,

        @Email(message = "email must be valid")
        String email,

        @Size(max = 50)
        String phone,

        String notes,

        @Min(value = 0, message = "lessonsRemaining must be >= 0")
        Integer lessonsRemaining,

        Long pkgId
) {}
