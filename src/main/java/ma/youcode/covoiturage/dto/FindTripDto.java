package ma.youcode.covoiturage.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FindTripDto(

        BigDecimal prix,
        LocalDateTime departTime,
        LocalDateTime arrivalTime

) {
}
