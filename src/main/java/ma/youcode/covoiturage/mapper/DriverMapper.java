package ma.youcode.covoiturage.mapper;

import ma.youcode.covoiturage.dto.CreateDriverReq;
import ma.youcode.covoiturage.dto.CreateDriverRes;
import ma.youcode.covoiturage.entity.Driver;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DriverMapper {

     Driver toEntity(CreateDriverReq req);
     CreateDriverRes toDto(Driver driver);
}
