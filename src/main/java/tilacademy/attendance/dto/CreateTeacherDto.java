package tilacademy.attendance.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateTeacherDto(
        @NotBlank String firstName,
        @NotBlank String lastName,
        @Email String email,
        String phone,
        /**
         * optional username for login; if null/blank, server will use email as username.
         */
        String username,
        /**
         * optional raw password. If null/blank the server will auto-generate a secure password.
         */
        String password
) {}
