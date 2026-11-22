package ma.youcode.covoiturage.dto;

public record CreatePassengerRes(
        Long id,
        String nom,
        String prenom,
        String phoneNumber
) { }
