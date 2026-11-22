package ma.youcode.covoiturage.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record StopPointRes(
        Integer steps,
        String ville,
        LocalDateTime heurePassage,
        BigDecimal prixDepuitsDepart
) {
}
