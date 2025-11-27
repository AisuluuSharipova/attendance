package tilacademy.attendance.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import tilacademy.attendance.dto.PkgDto;
import tilacademy.attendance.dto.CreatePkgDto;
import tilacademy.attendance.dto.UpdatePkgDto;
import tilacademy.attendance.entities.Pkg;

@Mapper(componentModel = "spring")
public interface PkgMapper {
    PkgDto toDto(Pkg pkg);

    @Mapping(target = "id", ignore = true)
    Pkg toEntity(CreatePkgDto dto);

    @Mapping(target = "id", ignore = true)
    Pkg toEntity(UpdatePkgDto dto);
}

