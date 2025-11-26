package tilacademy.attendance.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tilacademy.attendance.entities.Teacher;
import tilacademy.attendance.dto.TeacherDto;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(target = "fullName", expression = "java(teacher.getFullName())")
    TeacherDto toDto(Teacher teacher);
}
