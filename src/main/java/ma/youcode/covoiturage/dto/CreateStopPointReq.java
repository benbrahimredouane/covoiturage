package ma.youcode.covoiturage.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateStopPointReq(

        @NotBlank(message = "ville is required")
        String ville ,
        @NotNull(message = "Passage time is required")
        LocalDateTime heurePassage,
        @NotNull(message = "prix is required ")
        @DecimalMin(value = "0.0" , message = "price can't be negative")
        BigDecimal prixDepuitsDepart

) {
}
