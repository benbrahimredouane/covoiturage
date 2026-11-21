package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.dto.CreateVehicleReq;
import ma.youcode.covoiturage.dto.DeleteVehicleReq;
import ma.youcode.covoiturage.dto.ModifierVehicleReq;
import ma.youcode.covoiturage.entity.Vehicle;

import java.util.List;

public interface VehicleService {
    public Vehicle create(CreateVehicleReq req);
    public Vehicle findById(Long id , Long driverId);
    public Vehicle updateVehicle(Long id , ModifierVehicleReq req);
    public List<Vehicle> findByDriver(Long driverId);
    public void delete(Long id , DeleteVehicleReq req);
}
