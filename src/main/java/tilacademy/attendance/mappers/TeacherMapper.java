package tilacademy.attendance.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tilacademy.attendance.dto.TeacherDto;
import tilacademy.attendance.dto.CreateTeacherDto;
import tilacademy.attendance.dto.UpdateTeacherDto;
import tilacademy.attendance.entities.Teacher;

@Mapper(componentModel = "spring")
public interface TeacherMapper {

    @Mapping(target = "fullName", expression = "java(teacher.getFullName())")
    TeacherDto toDto(Teacher teacher);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Teacher toEntity(CreateTeacherDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Teacher toEntity(UpdateTeacherDto dto);
}
