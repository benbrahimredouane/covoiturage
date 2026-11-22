package ma.youcode.covoiturage.dto;

import ma.youcode.covoiturage.entity.enums.StatutReservation;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ReservationRes(
        Long id,
        Long passengerId,
        String passengerNom,
        String passengerPrenom,
        Long trajetId,
        String villeMontee,
        String villeDescente,
        Integer nbPlaces,
        BigDecimal montantTotal,
        StatutReservation statut,
        LocalDateTime dateReservation
) {
}
