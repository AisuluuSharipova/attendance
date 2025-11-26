package tilacademy.attendance.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tilacademy.attendance.entities.Student;
import tilacademy.attendance.dto.StudentListDto;
import tilacademy.attendance.dto.StudentDetailDto;

@Mapper(componentModel = "spring")
public interface StudentMapper {

    // DTO для таблицы студентов
    @Mapping(target = "fullName", expression = "java(student.getFullName())")
    @Mapping(target = "lessonsRemaining", expression = "java(student.getRemainingLessons())")
    @Mapping(target = "lowOnLessons", expression = "java(student.isLowOnLessons())")
    @Mapping(target = "pkgId", source = "pkg.id")
    @Mapping(target = "pkgName", source = "pkg.name")
    StudentListDto toListDto(Student student);

    // DTO для карточки студента
    @Mapping(target = "fullName", expression = "java(student.getFullName())")
    @Mapping(target = "lessonsRemaining", expression = "java(student.getRemainingLessons())")
    @Mapping(target = "lowOnLessons", expression = "java(student.isLowOnLessons())")
    @Mapping(target = "pkgId", source = "pkg.id")
    @Mapping(target = "pkgName", source = "pkg.name")
    StudentDetailDto toDetailDto(Student student);
}
