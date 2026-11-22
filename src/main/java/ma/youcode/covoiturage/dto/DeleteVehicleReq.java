package ma.youcode.covoiturage.dto;

import jakarta.validation.constraints.NotNull;

public record DeleteVehicleReq(
        @NotNull(message = "driver id is required")
        Long driverId
) {
}
