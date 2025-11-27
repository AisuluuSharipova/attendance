package tilacademy.attendance.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public record CreateAttendanceDto(
        @NotNull(message = "studentId is required")
        Long studentId,

        Long teacherId,

        @NotNull(message = "attendanceDate is required")
        LocalDate attendanceDate,

        @NotBlank(message = "status is required")
        @Pattern(regexp = "PRESENT|ABSENT|LATE", message = "status must be PRESENT, ABSENT or LATE")
        String status,

        String note
) {}
