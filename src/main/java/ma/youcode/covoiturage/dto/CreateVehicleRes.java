package ma.youcode.covoiturage.dto;

public record CreateVehicleRes(
Long id,
String marque,
String modele,
String immatriculation,
int capacite,
Long driverId
) {
}
