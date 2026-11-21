package ma.youcode.covoiturage.service;

import lombok.extern.slf4j.Slf4j;
import ma.youcode.covoiturage.common.exception.BusinessRuleException;
import ma.youcode.covoiturage.common.exception.DriverNotFoundException;
import ma.youcode.covoiturage.common.exception.MatriculeNotUniqueException;
import ma.youcode.covoiturage.common.exception.NotYourVehicleException;
import ma.youcode.covoiturage.common.exception.VehicleNotFoundException;
import ma.youcode.covoiturage.dto.CreateVehicleReq;
import ma.youcode.covoiturage.dto.DeleteVehicleReq;
import ma.youcode.covoiturage.dto.ModifierVehicleReq;
import ma.youcode.covoiturage.entity.Driver;
import ma.youcode.covoiturage.entity.Vehicle;
import ma.youcode.covoiturage.mapper.VehicleMapper;
import ma.youcode.covoiturage.repository.DriverRepository;
import ma.youcode.covoiturage.repository.TrajetRepository;
import ma.youcode.covoiturage.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VehicleServiceImp implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final DriverRepository driverRepository;
    private final TrajetRepository trajetRepository;
    public VehicleServiceImp(VehicleRepository vehicleRepository ,VehicleMapper vehicleMapper ,DriverRepository driverRepository ,TrajetRepository trajetRepository) {
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
        this.vehicleMapper = vehicleMapper;
        this.trajetRepository = trajetRepository;
    }


    @Transactional
    public Vehicle create(CreateVehicleReq req) {
        Optional<Vehicle> vehicle = vehicleRepository.findByImmatriculation(req.immatriculation());
        if (vehicle.isPresent()) {
            throw new MatriculeNotUniqueException("this matricule is taken");
        }
        Optional<Driver> driver = driverRepository.findById(req.driverId());
        if (driver.isEmpty()) {
            throw new DriverNotFoundException("Driver with id " + req.driverId() + " not exists");
        }

        Vehicle vehicle1 = vehicleMapper.toEntity(req);
        vehicle1.setDriver(driver.get());
        Vehicle saved = vehicleRepository.save(vehicle1);
        log.info("vehicle {} added by driver {}", saved.getId(), req.driverId());
        return saved;

    }

    // DRIVER : consult only HIS vehicle
    @Transactional
    public Vehicle findById(Long id , Long driverId){
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(()->
                new VehicleNotFoundException("vehicle with id "+id+" not exists"));
        checkOwner(vehicle, driverId);
        return vehicle;
    }

    // DRIVER : list HIS vehicles
    @Transactional(readOnly = true)
    public List<Vehicle> findByDriver(Long driverId){
        if (!driverRepository.existsById(driverId)){
            throw new DriverNotFoundException("Driver with id "+driverId+" not exists");
        }
        return vehicleRepository.findByDriverId(driverId);
    }

    // DRIVER : update only HIS vehicle
    @Transactional
    public Vehicle updateVehicle(Long id , ModifierVehicleReq req){
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(()->
                new VehicleNotFoundException("vehicle with "+ id +" not found"));
        checkOwner(vehicle, req.driverId());

        if(req.marque()!= null && !req.marque().isBlank() ){
            vehicle.setMarque(req.marque());
        }
        if (req.modele()!=null && !req.modele().isBlank()){
            vehicle.setModele(req.modele());
        }
        if (req.capacite() != null){
            vehicle.setCapacite(req.capacite());
        }

        Vehicle saved = vehicleRepository.save(vehicle);
        log.info("vehicle {} updated by driver {}", id, req.driverId());
        return saved;
    }

    // DRIVER : delete only HIS vehicle, and only if no trajet uses it
    @Transactional
    public void delete(Long id , DeleteVehicleReq req){
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(()->
                new VehicleNotFoundException("Vehicle with id "+id+ " not exist"));
        checkOwner(vehicle, req.driverId());

        if (trajetRepository.existsByVehicleId(id)){
            throw new BusinessRuleException("this vehicle is used by a trajet , it can't be deleted");
        }
        vehicleRepository.delete(vehicle);
        log.info("vehicle {} deleted by driver {}", id, req.driverId());
    }

    private void checkOwner(Vehicle vehicle , Long driverId){
        if (vehicle.getDriver() == null || !vehicle.getDriver().getId().equals(driverId)){
            throw new NotYourVehicleException("this vehicle is not yours");
        }
    }
}
