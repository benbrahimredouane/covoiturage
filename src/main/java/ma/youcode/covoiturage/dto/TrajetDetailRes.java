package ma.youcode.covoiturage.dto;

import ma.youcode.covoiturage.entity.enums.StatutTrajet;

import java.time.LocalDateTime;
import java.util.List;

public record TrajetDetailRes(
        Long id,
        String driverNom,
        String driverPrenom,
        String vehicleMarque,
        String vehicleModele,
        String startVille,
        String endVille,
        LocalDateTime startDate,
        Integer places,
        Long placesRestantes,
        StatutTrajet statutTrajet,
        List<StopPointRes> stopPoints
) {
}
