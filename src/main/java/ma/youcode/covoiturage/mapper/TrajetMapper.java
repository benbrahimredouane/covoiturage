package ma.youcode.covoiturage.mapper;

import ma.youcode.covoiturage.dto.CreateStopPointReq;
import ma.youcode.covoiturage.dto.CreateTrajetReq;
import ma.youcode.covoiturage.dto.CreateTrajetRes;
import ma.youcode.covoiturage.dto.FindTripDto;
import ma.youcode.covoiturage.entity.StopPoint;
import ma.youcode.covoiturage.entity.Trajet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TrajetMapper {

    public CreateTrajetRes toDto(Trajet trajet);

    @Mapping(target = "driver",ignore = true)
    @Mapping(target = "vehicle", ignore= true)
    public Trajet toEntity(CreateTrajetReq req);

    @Mapping(target = "trajet",ignore = true)
    @Mapping(target = "steps" , ignore = true)
    StopPoint toEntity(CreateStopPointReq req);

    @Mapping(target = "prix", expression = "java(arrivee.getPrixDepuitsDepart().subtract(depart.getPrixDepuitsDepart()))")
    @Mapping(target = "departTime", source = "depart.heurePassage")
    @Mapping(target = "arrivalTime", source = "arrivee.heurePassage")
    FindTripDto toFindTripDto(Trajet trajet, StopPoint depart, StopPoint arrivee);
}
