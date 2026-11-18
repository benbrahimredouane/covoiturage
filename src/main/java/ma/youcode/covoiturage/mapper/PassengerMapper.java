package ma.youcode.covoiturage.mapper;

import ma.youcode.covoiturage.entity.Passenger;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PassengerMapper {

    public Passenger toEntity();
    public CreatePassengerRes toDto(Passenger passenger);
}
