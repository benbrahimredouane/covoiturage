package ma.youcode.covoiturage.dto;

import ma.youcode.covoiturage.entity.enums.StatutTrajet;

import java.time.LocalDateTime;

public record TrajetSummaryRes(
        Long id,
        String startVille,
        String endVille,
        LocalDateTime startDate,
        Integer places,
        StatutTrajet statutTrajet
) {
}
