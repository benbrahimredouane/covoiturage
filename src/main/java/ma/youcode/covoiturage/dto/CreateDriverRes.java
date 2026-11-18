package ma.youcode.covoiturage.dto;

public record CreateDriverRes(
        //    id, nom, prénom, email, téléphone, numéro de permis
        Long id,
        String nom,
        String prenom,
        String phoneNumber,
        String permisN
)
{ }
