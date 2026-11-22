package ma.youcode.covoiturage.controller;

import jakarta.validation.Valid;
import ma.youcode.covoiturage.dto.CreateTrajetReq;
import ma.youcode.covoiturage.dto.CreateTrajetRes;
import ma.youcode.covoiturage.dto.CreateVehicleRes;
import ma.youcode.covoiturage.dto.FindTripDto;
import ma.youcode.covoiturage.entity.Trajet;
import ma.youcode.covoiturage.mapper.TrajetMapper;
import ma.youcode.covoiturage.service.TrajetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

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
    @GetMapping("/trajets")
    public ResponseEntity<Page<FindTripDto>> searchByStartVilleEndVilleAndDate(
            @RequestParam(name = "startVille") String startVille,
            @RequestParam(name = "endVille") String endVille,
            @RequestParam(name = "date") LocalDateTime date,
            @PageableDefault(size = 10) Pageable pageable
    ) {

        Page<FindTripDto> res = trajetService.search(
                startVille,
                endVille,
                date,
                pageable
        );

        return ResponseEntity.ok(res);
    }
}
