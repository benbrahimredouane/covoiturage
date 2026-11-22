package ma.youcode.covoiturage.dto;

import jakarta.validation.constraints.NotNull;

public record DeleteVehicleReq(
        @NotNull
        Long driverId
) {
}
