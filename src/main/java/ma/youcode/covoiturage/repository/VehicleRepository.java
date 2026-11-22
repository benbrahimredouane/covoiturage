package ma.youcode.covoiturage.repository;

import ma.youcode.covoiturage.dto.CreateVehicleReq;
import ma.youcode.covoiturage.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface VehicleRepository extends JpaRepository<Vehicle,Long> {
    Optional<Vehicle> findByImmatriculation(String immatriculation);
    List<Vehicle> findByDriverId(Long driverId);
}
