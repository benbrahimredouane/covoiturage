package ma.youcode.covoiturage.repository;

import ma.youcode.covoiturage.entity.Passenger;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PassengerRepository extends JpaRepository<Passenger,Long> {
}
