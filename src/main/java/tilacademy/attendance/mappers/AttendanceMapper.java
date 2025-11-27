package tilacademy.attendance.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tilacademy.attendance.dto.AttendanceDto;
import tilacademy.attendance.dto.CreateAttendanceDto;
import tilacademy.attendance.entities.Attendance;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "studentFullName", expression = "java(attendance.getStudent().getFullName())")
    @Mapping(target = "teacherId", source = "teacher.id")
    @Mapping(target = "teacherFullName",
            expression = "java(attendance.getTeacher() != null ? attendance.getTeacher().getFullName() : null)")
    @Mapping(target = "status", expression = "java(attendance.getStatus().name())")
    AttendanceDto toDto(Attendance attendance);

    @Mapping(target = "student", ignore = true)
    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "status", ignore = true)
    Attendance toEntity(CreateAttendanceDto dto);
}
