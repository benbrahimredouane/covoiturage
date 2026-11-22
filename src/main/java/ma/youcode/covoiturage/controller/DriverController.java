package ma.youcode.covoiturage.controller;

import jakarta.validation.Valid;
import ma.youcode.covoiturage.dto.CreateDriverReq;
import ma.youcode.covoiturage.dto.CreateDriverRes;
import ma.youcode.covoiturage.entity.Driver;
import ma.youcode.covoiturage.mapper.DriverMapper;
import ma.youcode.covoiturage.service.DriverService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DriverController {
    private final DriverService driverService;
    private final DriverMapper driverMapper;

    public DriverController(DriverService driverService, DriverMapper driverMapper) {
        this.driverService = driverService;
        this.driverMapper = driverMapper;
    }

    @PostMapping("/drivers")
    public ResponseEntity<CreateDriverRes> createDriver(@Valid @RequestBody CreateDriverReq req){
        Driver driver =  driverService.createDriver(req);
        CreateDriverRes res = driverMapper.toDto(driver);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
