package ma.youcode.covoiturage.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateReservationReq(

        @NotNull(message = "passenger ID is required")
        Long passengerId,

        @NotNull(message = "trajet ID is required")
        Long trajetId,

        @NotBlank(message = "montee ville is required")
        String villeMontee,

        @NotBlank(message = "descente ville is required")
        String villeDescente,

        @NotNull(message = "places are required")
        @Min(value = 1, message = "at least 1 place is required")
        Integer nbPlaces

) {
}