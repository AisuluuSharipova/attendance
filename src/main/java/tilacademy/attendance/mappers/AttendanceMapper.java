package tilacademy.attendance.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tilacademy.attendance.entities.Attendance;
import tilacademy.attendance.dto.AttendanceDto;

@Mapper(componentModel = "spring")
public interface AttendanceMapper {

    @Mapping(target = "studentId", source = "student.id")
    @Mapping(target = "studentFullName", expression = "java(attendance.getStudent().getFullName())")
    @Mapping(target = "teacherId", source = "teacher.id")
    @Mapping(target = "teacherFullName",
            expression = "java(attendance.getTeacher() != null ? attendance.getTeacher().getFullName() : null)")
    @Mapping(target = "status", expression = "java(attendance.getStatus().name())")
    AttendanceDto toDto(Attendance attendance);
}
