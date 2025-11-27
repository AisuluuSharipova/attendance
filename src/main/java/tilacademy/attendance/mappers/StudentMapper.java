package tilacademy.attendance.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tilacademy.attendance.dto.StudentListDto;
import tilacademy.attendance.dto.StudentDetailDto;
import tilacademy.attendance.dto.CreateStudentDto;
import tilacademy.attendance.dto.UpdateStudentDto;
import tilacademy.attendance.entities.Student;

@Mapper(componentModel = "spring", uses = { PkgMapper.class })
public interface StudentMapper {

    @Mapping(target = "fullName", expression = "java(student.getFullName())")
    @Mapping(target = "lessonsRemaining", expression = "java(student.getRemainingLessons())")
    @Mapping(target = "lowOnLessons", expression = "java(student.isLowOnLessons())")
    @Mapping(target = "pkgId", source = "pkg.id")
    @Mapping(target = "pkgName", source = "pkg.name")
    StudentListDto toListDto(Student student);

    @Mapping(target = "fullName", expression = "java(student.getFullName())")
    @Mapping(target = "lowOnLessons", expression = "java(student.isLowOnLessons())")
    @Mapping(target = "pkgId", source = "pkg.id")
    @Mapping(target = "pkgName", source = "pkg.name")
    StudentDetailDto toDetailDto(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pkg", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Student toEntity(CreateStudentDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pkg", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    Student toEntity(UpdateStudentDto dto);
}
