package ma.youcode.covoiturage.controller;

import jakarta.validation.Valid;
import ma.youcode.covoiturage.dto.CreateTrajetReq;
import ma.youcode.covoiturage.dto.CreateTrajetRes;
import ma.youcode.covoiturage.dto.FindTripDto;
import ma.youcode.covoiturage.dto.ReservationRes;
import ma.youcode.covoiturage.dto.TrajetDetailRes;
import ma.youcode.covoiturage.dto.TrajetSummaryRes;
import ma.youcode.covoiturage.entity.Trajet;
import ma.youcode.covoiturage.mapper.TrajetMapper;
import ma.youcode.covoiturage.service.TrajetService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")
public class TrajetController {
    private final TrajetService trajetService;
    private final TrajetMapper trajetMapper;

    public TrajetController(TrajetService trajetService, TrajetMapper trajetMapper) {
        this.trajetService = trajetService;
        this.trajetMapper = trajetMapper;
    }

    // ---------- DRIVER
    @PostMapping("/trajets")
    public ResponseEntity<CreateTrajetRes> create(@Valid @RequestBody CreateTrajetReq req){
        Trajet trajet = trajetService.create(req);
        CreateTrajetRes res = trajetMapper.toDto(trajet);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
    @GetMapping("/drivers/{driverId}/trajets")
    public ResponseEntity<List<TrajetSummaryRes>> findByDriver(@PathVariable("driverId") Long driverId){
        return ResponseEntity.ok(trajetService.findByDriver(driverId));
    }
    @GetMapping("/trajets/{id}/reservations")
    public ResponseEntity<List<ReservationRes>> findReservations(
            @PathVariable("id") Long id,
            @RequestParam(name = "driverId") Long driverId){
        return ResponseEntity.ok(trajetService.findReservations(id, driverId));
    }
    @PatchMapping("/trajets/{id}/terminer")
    public ResponseEntity<Void> terminer(
            @PathVariable("id") Long id,
            @RequestParam(name = "driverId") Long driverId){
        trajetService.terminer(id, driverId);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/trajets/{id}/annuler")
    public ResponseEntity<Void> annuler(
            @PathVariable("id") Long id,
            @RequestParam(name = "driverId") Long driverId){
        trajetService.annuler(id, driverId);
        return ResponseEntity.noContent().build();
    }

    // ---------- PASSENGER (and anyone) : search and consult
    @GetMapping("/trajets")
    public ResponseEntity<Page<FindTripDto>> searchByStartVilleEndVilleAndDate(
            @RequestParam(name = "startVille") String startVille,
            @RequestParam(name = "endVille") String endVille,
            @RequestParam(name = "date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(name = "prixMax", required = false) BigDecimal prixMax,
            @RequestParam(name = "nbPlaces", defaultValue = "1") Integer nbPlaces,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Page<FindTripDto> res = trajetService.search(startVille, endVille, date, prixMax, nbPlaces, pageable);
        return ResponseEntity.ok(res);
    }
    @GetMapping("/trajets/{id}")
    public ResponseEntity<TrajetDetailRes> findDetail(@PathVariable("id") Long id){
        return ResponseEntity.ok(trajetService.findDetail(id));
    }
}
