package ma.youcode.covoiturage.repository;

import ma.youcode.covoiturage.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DriverRepository extends JpaRepository<Driver,Long> {
}
