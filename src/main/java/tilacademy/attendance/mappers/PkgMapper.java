package tilacademy.attendance.mappers;

import org.mapstruct.Mapper;
import tilacademy.attendance.dto.PkgDto;
import tilacademy.attendance.dto.CreatePkgDto;
import tilacademy.attendance.dto.UpdatePkgDto;
import tilacademy.attendance.entities.Pkg;

@Mapper(componentModel = "spring")
public interface PkgMapper {
    PkgDto toDto(Pkg pkg);
    Pkg toEntity(CreatePkgDto dto);
    Pkg toEntity(UpdatePkgDto dto);
}
