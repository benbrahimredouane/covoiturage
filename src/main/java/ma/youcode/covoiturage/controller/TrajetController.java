package ma.youcode.covoiturage.controller;

import jakarta.validation.Valid;
import ma.youcode.covoiturage.dto.CreateTrajetReq;
import ma.youcode.covoiturage.dto.CreateTrajetRes;
import ma.youcode.covoiturage.dto.CreateVehicleRes;
import ma.youcode.covoiturage.entity.Trajet;
import ma.youcode.covoiturage.mapper.TrajetMapper;
import ma.youcode.covoiturage.service.TrajetService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class TrajetController {
    private final TrajetService trajetService;
    private final TrajetMapper trajetMapper;

    public TrajetController(TrajetService trajetService, TrajetMapper trajetMapper) {
        this.trajetService = trajetService;
        this.trajetMapper = trajetMapper;
    }
    @PostMapping("/trajets")
    public ResponseEntity<CreateTrajetRes> create(@Valid @RequestBody CreateTrajetReq req){
        Trajet trajet = trajetService.create(req);
        CreateTrajetRes res = trajetMapper.toDto(trajet);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
}
