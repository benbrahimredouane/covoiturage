package ma.youcode.covoiturage.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FindTripDto(

        Long trajetId,
        String startVille,
        String endVille,
        LocalDateTime departTime,
        LocalDateTime arrivalTime,
        BigDecimal prix,
        Long placesRestantes

) {
}
