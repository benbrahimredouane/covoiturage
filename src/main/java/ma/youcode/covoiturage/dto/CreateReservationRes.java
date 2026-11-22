package ma.youcode.covoiturage.dto;

import ma.youcode.covoiturage.entity.enums.StatutReservation;
import ma.youcode.covoiturage.entity.enums.StatutTrajet;

import java.math.BigDecimal;

public record CreateReservationRes(
        Long id,
        Long passengerId,
        Long trajetId,
        String villeMontee,
        String villeDescente,
        Integer nbPlaces,
        BigDecimal montantTotal,
        StatutReservation statut,
        StatutTrajet statutTrajet,
        Integer placesRestantes
) {
}