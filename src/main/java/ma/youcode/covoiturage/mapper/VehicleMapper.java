package ma.youcode.covoiturage.mapper;

import ma.youcode.covoiturage.dto.CreateVehicleReq;
import ma.youcode.covoiturage.dto.CreateVehicleRes;
import ma.youcode.covoiturage.entity.Vehicle;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface VehicleMapper {

    public Vehicle toEntity(CreateVehicleReq req);

    @Mapping(source = "driver.id" ,target = "driverId")
    public CreateVehicleRes toDto(Vehicle vehicle);
}
