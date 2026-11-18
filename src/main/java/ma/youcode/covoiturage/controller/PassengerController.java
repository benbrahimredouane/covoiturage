package ma.youcode.covoiturage.controller;

import jakarta.validation.Valid;
import ma.youcode.covoiturage.dto.CreatePassengerReq;
import ma.youcode.covoiturage.dto.CreatePassengerRes;
import ma.youcode.covoiturage.entity.Passenger;
import ma.youcode.covoiturage.mapper.PassengerMapper;
import ma.youcode.covoiturage.service.PassengerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PassengerController {
    private final PassengerService passengerService;
    private final PassengerMapper passengerMapper;

    public PassengerController(PassengerService passengerService, PassengerMapper passengerMapper) {
        this.passengerService = passengerService;
        this.passengerMapper = passengerMapper;
    }

    @PostMapping("/passengers")
    public ResponseEntity<CreatePassengerRes> create(@Valid @RequestBody CreatePassengerReq req){
        Passenger res = passengerService.createPassenger(req);
        CreatePassengerRes response = passengerMapper.toDto(res);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }



}
