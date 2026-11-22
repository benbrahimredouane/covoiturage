package ma.youcode.covoiturage.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record ModifierVehicleReq(

        String marque,

        String modele,

        String immatriculation,


        @Min(value = 1, message = "capacity must be greater that 1")
        @Max(value = 8 , message = "capacity must be less than 8 ")
        Integer capacite,

        @NotNull(message = "driver ID is required ")
        Long driverId
) {
}
