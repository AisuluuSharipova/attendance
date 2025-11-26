package tilacademy.attendance.dto;

import java.time.LocalDate;

public record AttendanceDto(
        Long id,
        Long studentId,
        String studentFullName,
        Long teacherId,
        String teacherFullName,
        LocalDate attendanceDate,
        String status,
        String note
) {}
