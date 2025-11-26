package tilacademy.attendance.dto;

import java.math.BigDecimal;

public record PkgDto(
        Long id,
        String name,
        String description,
        Integer lessonsCount,
        Integer durationDays,
        BigDecimal price,
        Boolean isActive
) {}
