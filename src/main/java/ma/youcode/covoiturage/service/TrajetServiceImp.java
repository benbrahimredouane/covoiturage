package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.common.exception.DriverNotFoundException;
import ma.youcode.covoiturage.common.exception.NotYourVehicleException;
import ma.youcode.covoiturage.common.exception.VehicleNotFoundException;
import ma.youcode.covoiturage.dto.CreateTrajetReq;
import ma.youcode.covoiturage.entity.Driver;
import ma.youcode.covoiturage.entity.StopPoint;
import ma.youcode.covoiturage.entity.Trajet;
import ma.youcode.covoiturage.entity.Vehicle;
import ma.youcode.covoiturage.mapper.TrajetMapper;
import ma.youcode.covoiturage.repository.DriverRepository;
import ma.youcode.covoiturage.repository.TrajetRepository;
import ma.youcode.covoiturage.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TrajetServiceImp implements  TrajetService{
    private final TrajetRepository trajetRepository;
    private final TrajetMapper trajetMapper;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;

    public TrajetServiceImp(TrajetRepository trajetRepository, TrajetMapper trajetMapper ,DriverRepository driverRepository ,VehicleRepository vehicleRepository) {
        this.trajetRepository = trajetRepository;
        this.trajetMapper = trajetMapper;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public Trajet create(CreateTrajetReq req){
        Driver driver = driverRepository.findById(req.driverId()).orElseThrow(()->
                new DriverNotFoundException("this driver with id "+req.driverId()+" not exist"));
        Vehicle vehicle = vehicleRepository.findById(req.vehiculeId()).orElseThrow(()->
                new VehicleNotFoundException("vehicle with id "+req.vehiculeId()+" not exists"));

        if (!vehicle.getDriver().getId().equals(driver.getId())){
            throw new NotYourVehicleException("this vehicle is not yours");
        }

        Trajet trajet = trajetMapper.toEntity(req);
        trajet.setDriver(driver);
        trajet.setVehicle(vehicle);
        List<StopPoint> stops = new ArrayList<>(trajet.getStopPoints());
        trajet.getStopPoints().clear();
        for (int i =0 ; i< stops.size() ; i++){
            stops.get(i).setSteps(i);
            trajet.addStopPoint(stops.get(i));
//            stopPoint.trajet = trajet;

        }
        return trajetRepository.save(trajet);
    }
}
