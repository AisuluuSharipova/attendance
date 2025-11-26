package tilacademy.attendance.dto;

public record StudentListDto(
        Long id,
        String fullName,
        String email,
        Integer lessonsRemaining,
        Boolean lowOnLessons,
        Long pkgId,
        String pkgName
) {}
