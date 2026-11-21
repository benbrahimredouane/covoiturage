package ma.youcode.covoiturage.mapper;

import ma.youcode.covoiturage.dto.CreateStopPointReq;
import ma.youcode.covoiturage.dto.CreateTrajetReq;
import ma.youcode.covoiturage.dto.CreateTrajetRes;
import ma.youcode.covoiturage.dto.StopPointRes;
import ma.youcode.covoiturage.dto.TrajetDetailRes;
import ma.youcode.covoiturage.dto.TrajetSummaryRes;
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

    public TrajetSummaryRes toSummary(Trajet trajet);

    public StopPointRes toStopPointRes(StopPoint stopPoint);

    @Mapping(source = "trajet.id", target = "id")
    @Mapping(source = "trajet.driver.nom", target = "driverNom")
    @Mapping(source = "trajet.driver.prenom", target = "driverPrenom")
    @Mapping(source = "trajet.vehicle.marque", target = "vehicleMarque")
    @Mapping(source = "trajet.vehicle.modele", target = "vehicleModele")
    @Mapping(source = "trajet.startVille", target = "startVille")
    @Mapping(source = "trajet.endVille", target = "endVille")
    @Mapping(source = "trajet.startDate", target = "startDate")
    @Mapping(source = "trajet.places", target = "places")
    @Mapping(source = "placesRestantes", target = "placesRestantes")
    @Mapping(source = "trajet.statutTrajet", target = "statutTrajet")
    @Mapping(source = "trajet.stopPoints", target = "stopPoints")
    public TrajetDetailRes toDetail(Trajet trajet , Long placesRestantes);
}
