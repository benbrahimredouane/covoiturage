package ma.youcode.covoiturage.controller;

import jakarta.validation.Valid;
import ma.youcode.covoiturage.common.exception.DriverNotFoundException;
import ma.youcode.covoiturage.dto.CreateVehicleReq;
import ma.youcode.covoiturage.dto.CreateVehicleRes;
import ma.youcode.covoiturage.dto.DeleteVehicleReq;
import ma.youcode.covoiturage.dto.ModifierVehicleReq;
import ma.youcode.covoiturage.entity.Driver;
import ma.youcode.covoiturage.entity.Vehicle;
import ma.youcode.covoiturage.mapper.VehicleMapper;
import ma.youcode.covoiturage.service.VehicleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class VehicleController {
    private final VehicleMapper vehicleMapper;
    private final VehicleService vehicleService;

    public VehicleController(VehicleMapper vehicleMapper, VehicleService vehicleService) {
        this.vehicleMapper = vehicleMapper;
        this.vehicleService = vehicleService;
    }
//    Ajouter, consulter, modifier et lister les véhicules d'un conducteur.
    @PostMapping("/vehicles")
    public ResponseEntity<CreateVehicleRes> create(@Valid @RequestBody CreateVehicleReq req){

        Vehicle vehicle = vehicleService.create(req);
        CreateVehicleRes response = vehicleMapper.toDto(vehicle);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @GetMapping("/vehicles/{id}")
    public ResponseEntity<CreateVehicleRes> findVehicle(@PathVariable("id") Long id){
        Vehicle vehicle = vehicleService.findById(id);
        CreateVehicleRes res = vehicleMapper.toDto(vehicle);

        return ResponseEntity.status(HttpStatus.OK).body(res);
    }
    @PutMapping("/vehicles/{id}")
    public ResponseEntity<Vehicle> updateVehicle(@PathVariable("id") Long id ,@Valid @RequestBody ModifierVehicleReq req){
        Vehicle vehicle = vehicleService.updateVehicle(id,req);
        return ResponseEntity.status(HttpStatus.OK).body(vehicle);
    }
    @GetMapping("/vehicles")
        public ResponseEntity<List<Vehicle>> findAll(){
            List<Vehicle> vehicleList = vehicleService.findthem();
            return ResponseEntity.status(HttpStatus.OK).body(vehicleList);


    }
    @DeleteMapping("/vehicles/{id}")
        public ResponseEntity<Void> deletevehicle(@PathVariable("id") Long id , @RequestBody DeleteVehicleReq req){

            vehicleService.delete(id,req);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }



}
