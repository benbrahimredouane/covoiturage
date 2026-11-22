package ma.youcode.covoiturage.controller;

import jakarta.validation.Valid;
import ma.youcode.covoiturage.dto.CreateReservationReq;
import ma.youcode.covoiturage.dto.CreateReservationRes;
import ma.youcode.covoiturage.dto.ReservationRes;
import ma.youcode.covoiturage.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    // ---------- PASSENGER only
    @PostMapping("/reservations")
    public ResponseEntity<CreateReservationRes> reserve(@Valid @RequestBody CreateReservationReq req){
        CreateReservationRes res = reservationService.reserve(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }
    @GetMapping("/reservations/{id}")
    public ResponseEntity<ReservationRes> findById(
            @PathVariable("id") Long id,
            @RequestParam(name = "passengerId") Long passengerId){
        return ResponseEntity.ok(reservationService.findById(id, passengerId));
    }
    @GetMapping("/passengers/{passengerId}/reservations")
    public ResponseEntity<List<ReservationRes>> findByPassenger(@PathVariable("passengerId") Long passengerId){
        return ResponseEntity.ok(reservationService.findByPassenger(passengerId));
    }
    @PatchMapping("/reservations/{id}/annuler")
    public ResponseEntity<Void> cancel(
            @PathVariable("id") Long id,
            @RequestParam(name = "passengerId") Long passengerId){
        reservationService.cancel(id, passengerId);
        return ResponseEntity.noContent().build();
    }
}
