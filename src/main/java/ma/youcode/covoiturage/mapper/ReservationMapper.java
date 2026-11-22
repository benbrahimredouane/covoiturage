package ma.youcode.covoiturage.mapper;

import ma.youcode.covoiturage.dto.CreateReservationRes;
import ma.youcode.covoiturage.dto.ReservationRes;
import ma.youcode.covoiturage.entity.Reservation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReservationMapper {

    @Mapping(source = "reservation.id", target = "id")
    @Mapping(source = "reservation.passenger.id", target = "passengerId")
    @Mapping(source = "reservation.trajet.id", target = "trajetId")
    @Mapping(source = "reservation.pointMontee.ville", target = "villeMontee")
    @Mapping(source = "reservation.pointDescente.ville", target = "villeDescente")
    @Mapping(source = "reservation.nbPlaces", target = "nbPlaces")
    @Mapping(source = "reservation.montantTotal", target = "montantTotal")
    @Mapping(source = "reservation.statut", target = "statut")
    @Mapping(source = "reservation.trajet.statutTrajet", target = "statutTrajet")
    @Mapping(source = "placesRestantes", target = "placesRestantes")
    public CreateReservationRes toDto(Reservation reservation , Integer placesRestantes);

    @Mapping(source = "passenger.id", target = "passengerId")
    @Mapping(source = "passenger.nom", target = "passengerNom")
    @Mapping(source = "passenger.prenom", target = "passengerPrenom")
    @Mapping(source = "trajet.id", target = "trajetId")
    @Mapping(source = "pointMontee.ville", target = "villeMontee")
    @Mapping(source = "pointDescente.ville", target = "villeDescente")
    public ReservationRes toRes(Reservation reservation);
}