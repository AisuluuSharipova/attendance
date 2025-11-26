package tilacademy.attendance.dto;

import java.time.LocalDate;

public record CreateAttendanceDto(
        Long studentId,
        Long teacherId, // optional
        LocalDate attendanceDate,
        String status,
        String note
) {}
