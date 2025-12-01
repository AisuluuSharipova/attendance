package tilacademy.attendance.dto;

public record StudentDetailDto(
        Long id,
        String firstName,
        String lastName,
        String fullName,
        String email,
        String phone,
        String notes,
        Integer lessonsRemaining,
        Boolean lowOnLessons,
        Long pkgId,
        String pkgName,
        Long teacherId,
        String teacherName
) {}
