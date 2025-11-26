package tilacademy.attendance.mappers;

import org.mapstruct.Mapper;
import tilacademy.attendance.entities.Pkg;
import tilacademy.attendance.dto.PkgDto;

@Mapper(componentModel = "spring")
public interface PkgMapper {
    PkgDto toDto(Pkg pkg);
}
