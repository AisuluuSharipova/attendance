package tilacademy.attendance.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record UpdatePkgDto(
        @NotBlank(message = "Package name is required")
        @Size(max = 150)
        String name,

        String description,

        @NotNull(message = "lessonsCount is required")
        @Min(value = 1, message = "lessonsCount must be >= 1")
        Integer lessonsCount,

        @NotNull(message = "durationDays is required")
        @Min(value = 1, message = "durationDays must be >= 1")
        Integer durationDays,

        @NotNull(message = "price is required")
        @DecimalMin(value = "0.0", inclusive = true, message = "price must be >= 0")
        BigDecimal price,

        Boolean isActive
) {}
