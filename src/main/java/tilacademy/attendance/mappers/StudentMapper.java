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
    @Mapping(target = "teacherId", expression = "java(student.getTeacher() != null ? student.getTeacher().getId() : null)")
    @Mapping(target = "teacherName", expression = "java(student.getTeacher() != null ? student.getTeacher().getFullName() : null)")
    StudentListDto toListDto(Student student);

    @Mapping(target = "fullName", expression = "java(student.getFullName())")
    @Mapping(target = "lessonsRemaining", expression = "java(student.getRemainingLessons())")
    @Mapping(target = "lowOnLessons", expression = "java(student.isLowOnLessons())")
    @Mapping(target = "pkgId", source = "pkg.id")
    @Mapping(target = "pkgName", source = "pkg.name")
    @Mapping(target = "teacherId", expression = "java(student.getTeacher() != null ? student.getTeacher().getId() : null)")
    @Mapping(target = "teacherName", expression = "java(student.getTeacher() != null ? student.getTeacher().getFullName() : null)")
    StudentDetailDto toDetailDto(Student student);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pkg", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "teacher", ignore = true) // controller sets teacher explicitly
    Student toEntity(CreateStudentDto dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "pkg", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "teacher", ignore = true) // controller sets teacher explicitly
    Student toEntity(UpdateStudentDto dto);
}
