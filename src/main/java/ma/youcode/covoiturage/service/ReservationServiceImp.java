package ma.youcode.covoiturage.service;

import lombok.extern.slf4j.Slf4j;
import ma.youcode.covoiturage.common.exception.BusinessRuleException;
import ma.youcode.covoiturage.common.exception.ForbiddenActionException;
import ma.youcode.covoiturage.common.exception.InvalidSegmentException;
import ma.youcode.covoiturage.common.exception.PassengerNotFoundException;
import ma.youcode.covoiturage.common.exception.ReservationNotFoundException;
import ma.youcode.covoiturage.common.exception.TrajetNotFoundException;
import ma.youcode.covoiturage.dto.CreateReservationReq;
import ma.youcode.covoiturage.dto.CreateReservationRes;
import ma.youcode.covoiturage.dto.ReservationRes;
import ma.youcode.covoiturage.entity.Passenger;
import ma.youcode.covoiturage.entity.Reservation;
import ma.youcode.covoiturage.entity.StopPoint;
import ma.youcode.covoiturage.entity.Trajet;
import ma.youcode.covoiturage.entity.enums.StatutReservation;
import ma.youcode.covoiturage.entity.enums.StatutTrajet;
import ma.youcode.covoiturage.mapper.ReservationMapper;
import ma.youcode.covoiturage.repository.PassengerRepository;
import ma.youcode.covoiturage.repository.ReservationRepository;
import ma.youcode.covoiturage.repository.TrajetRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
public class ReservationServiceImp implements ReservationService {
    private final ReservationRepository reservationRepository;
    private final TrajetRepository trajetRepository;
    private final PassengerRepository passengerRepository;
    private final ReservationMapper reservationMapper;

    public ReservationServiceImp(ReservationRepository reservationRepository, TrajetRepository trajetRepository,
                                 PassengerRepository passengerRepository, ReservationMapper reservationMapper) {
        this.reservationRepository = reservationRepository;
        this.trajetRepository = trajetRepository;
        this.passengerRepository = passengerRepository;
        this.reservationMapper = reservationMapper;
    }

    @Transactional
    @Override
    public CreateReservationRes reserve(CreateReservationReq req){
        Passenger passenger = passengerRepository.findById(req.passengerId()).orElseThrow(()->
                new PassengerNotFoundException("passenger with id "+req.passengerId()+" not exists"));
        Trajet trajet = trajetRepository.findByIdForUpdate(req.trajetId()).orElseThrow(()->
                new TrajetNotFoundException("trajet with id "+req.trajetId()+" not exists"));

        // the two points must belong to the trajet, and montee must be before descente
        StopPoint montee = findStop(trajet, req.villeMontee());
        StopPoint descente = findStop(trajet, req.villeDescente());
        if (montee.getSteps() >= descente.getSteps()){
            throw new InvalidSegmentException("montee must be before descente on this trajet");
        }

        if (trajet.getStatutTrajet() != StatutTrajet.PUBLIE){
            throw new BusinessRuleException("this trajet is not open to reservation, status : "+trajet.getStatutTrajet());
        }

        if (reservationRepository.existsByPassengerIdAndTrajetIdAndStatut(passenger.getId(), trajet.getId(), StatutReservation.CONFIRMEE)){
            throw new BusinessRuleException("this passenger already has a confirmed reservation on this trajet");
        }

        int placesRestantes = trajet.getPlaces() - reservationRepository.countConfirmedPlaces(trajet.getId()).intValue();
        if (req.nbPlaces() > placesRestantes){
            throw new BusinessRuleException("only "+placesRestantes+" place(s) left on this trajet");
        }

        BigDecimal montant = descente.getPrixDepuitsDepart().subtract(montee.getPrixDepuitsDepart())
                .multiply(BigDecimal.valueOf(req.nbPlaces()))
                .setScale(2, RoundingMode.HALF_UP);

        Reservation reservation = new Reservation();
        reservation.setPassenger(passenger);
        reservation.setTrajet(trajet);
        reservation.setPointMontee(montee);
        reservation.setPointDescente(descente);
        reservation.setNbPlaces(req.nbPlaces());
        reservation.setMontantTotal(montant);
        reservation.setDateReservation(LocalDateTime.now());
        reservation.setStatut(StatutReservation.CONFIRMEE);
        reservationRepository.save(reservation);

        placesRestantes -= req.nbPlaces();
        if (placesRestantes == 0){
            trajet.setStatutTrajet(StatutTrajet.COMPLET);
            trajetRepository.save(trajet);
        }

        log.info("reservation {} confirmed : passenger {} , trajet {} , {} -> {} , {} place(s) , {} DH",
                reservation.getId(), passenger.getId(), trajet.getId(),
                montee.getVille(), descente.getVille(), req.nbPlaces(), montant);

        return reservationMapper.toDto(reservation, placesRestantes);
    }

    // PASSENGER : consult ONE reservation, only his own
    @Transactional(readOnly = true)
    @Override
    public ReservationRes findById(Long id , Long passengerId){
        Reservation reservation = reservationRepository.findById(id).orElseThrow(()->
                new ReservationNotFoundException("reservation with id "+id+" not exists"));
        checkOwner(reservation, passengerId);
        return reservationMapper.toRes(reservation);
    }

    // PASSENGER : consult his reservations
    @Transactional(readOnly = true)
    @Override
    public List<ReservationRes> findByPassenger(Long passengerId){
        if (!passengerRepository.existsById(passengerId)){
            throw new PassengerNotFoundException("passenger with id "+passengerId+" not exists");
        }
        return reservationRepository.findAllByPassengerId(passengerId).stream()
                .map(reservationMapper::toRes)
                .toList();
    }

    // PASSENGER : cancel HIS reservation, until 24 hours before the departure
    @Transactional
    @Override
    public void cancel(Long id , Long passengerId){
        Reservation reservation = reservationRepository.findById(id).orElseThrow(()->
                new ReservationNotFoundException("reservation with id "+id+" not exists"));
        checkOwner(reservation, passengerId);

        Trajet trajet = trajetRepository.findByIdForUpdate(reservation.getTrajet().getId()).orElseThrow(()->
                new TrajetNotFoundException("trajet with id "+reservation.getTrajet().getId()+" not exists"));

        if (reservation.getStatut() != StatutReservation.CONFIRMEE){
            throw new BusinessRuleException("this reservation is already cancelled");
        }
        if (LocalDateTime.now().isAfter(trajet.getStartDate().minusHours(24))){
            throw new BusinessRuleException("a reservation can be cancelled until 24 hours before the departure");
        }

        reservation.setStatut(StatutReservation.ANNULEE);
        reservationRepository.save(reservation);

        // a place is free again
        if (trajet.getStatutTrajet() == StatutTrajet.COMPLET){
            trajet.setStatutTrajet(StatutTrajet.PUBLIE);
            trajetRepository.save(trajet);
        }

        log.info("reservation {} cancelled by passenger {} , trajet {} is {}",
                id, passengerId, trajet.getId(), trajet.getStatutTrajet());
    }

    private void checkOwner(Reservation reservation , Long passengerId){
        if (!reservation.getPassenger().getId().equals(passengerId)){
            throw new ForbiddenActionException("this reservation is not yours");
        }
    }

    private StopPoint findStop(Trajet trajet , String ville){
        return trajet.getStopPoints().stream()
                .filter(stop -> stop.getVille().equalsIgnoreCase(ville))
                .findFirst()
                .orElseThrow(()-> new InvalidSegmentException(ville+" is not a stop of this trajet"));
    }
}