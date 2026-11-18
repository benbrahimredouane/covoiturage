package ma.youcode.covoiturage.mapper;

import ma.youcode.covoiturage.dto.CreatePassengerReq;
import ma.youcode.covoiturage.dto.CreatePassengerRes;
import ma.youcode.covoiturage.entity.Passenger;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PassengerMapper {

    public Passenger toEntity(CreatePassengerReq req);
    public CreatePassengerRes toDto(Passenger passenger);
}
