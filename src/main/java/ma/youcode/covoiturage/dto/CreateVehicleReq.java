package ma.youcode.covoiturage.dto;


import jakarta.validation.constraints.*;


public record CreateVehicleReq(
        @NotBlank(message = "marque must not be blank")
        String marque,
        @NotBlank(message = "modele must not be blank")
        String modele,
        @NotBlank( message = "immatricule obligatoire")
        String immatriculation,

        @NotNull(message = "capacity cannot be null")
        @Min(value = 1, message = "capacity must be greater that 1")
        @Max(value = 8 , message = "capacity must be less than 8 ")
        int capacite,
        @NotNull(message = "driver ID is required")
        Long driverId
) {
}
