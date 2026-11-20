package ma.youcode.covoiturage.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import ma.youcode.covoiturage.entity.StopPoint;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public record CreateTrajetReq(

        @NotNull(message = "driver ID is required")
        Long driverId,
        @NotNull(message = "vehicle ID is required")
        Long vehiculeId ,

        @NotBlank (message = "depart ville , city is required")
        String startVille,

        @NotBlank(message = "arrival city or ville is required ")
        String endVille,

        @NotNull(message = "depart date is required")
        LocalDateTime startDate,

        @NotNull(message = "places are required ")

        @Min(value = 1 , message = "at least 1 place required to create a trip")
        @Max(value = 8, message = "8 seats max")
        Integer places,

        @NotEmpty(message = "at least 2 stops are required")
        @Size(min = 2 , message = "a trajet at least needs 2 stops")

        List<@Valid CreateStopPointReq> stopPoints



) { }
