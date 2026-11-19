package ma.youcode.covoiturage.service;

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
import ma.youcode.covoiturage.repository.VehicleRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class VehicleServiceImp implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final VehicleMapper vehicleMapper;
    private final DriverRepository driverRepository;
    public VehicleServiceImp(VehicleRepository vehicleRepository ,VehicleMapper vehicleMapper ,DriverRepository driverRepository) {
        this.vehicleRepository = vehicleRepository;
        this.driverRepository = driverRepository;
        this.vehicleMapper = vehicleMapper;
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
        return vehicleRepository.save(vehicle1);

    }
    @Transactional
    public Vehicle findById(Long id){
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(()->
                new VehicleNotFoundException("vehicle with that id not exists"));
        return vehicle;
    };

    public List<Vehicle> findthem(){
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        return vehicleList;
    }

    @Transactional
    public Vehicle updateVehicle(Long id , ModifierVehicleReq req){
    Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(()->
            new VehicleNotFoundException("vehicle with "+ id +" not found"));

    if(req.marque()!= null && !req.marque().isBlank() ){
        vehicle.setMarque(req.marque());
    }
    if (req.modele()!=null && !req.modele().isBlank()){
        vehicle.setModele(req.modele());
    }
    if (req.capacite() != null){
        vehicle.setCapacite(req.capacite());

    }
    if (req.driverId() != null ){
        Driver driver = driverRepository.findById(req.driverId()).orElseThrow(()->
                new DriverNotFoundException("Driver with id "+req.driverId()+" not exists"));
        vehicle.setDriver(driver);
    }

    return vehicleRepository.save(vehicle);

}
@Transactional
    public void delete(Long id , DeleteVehicleReq req){
        Vehicle vehicle = vehicleRepository.findById(id).orElseThrow(()->
                new VehicleNotFoundException("Vehicle with id "+id+ " not exist"));
        if (vehicle.getDriver().getId() != req.driverId()){
            throw new NotYourVehicleException("Not your Vehcle to delete");
        }
        vehicleRepository.delete(vehicle);
}
}
