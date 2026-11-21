package ma.youcode.covoiturage.service;

import lombok.extern.slf4j.Slf4j;
import ma.youcode.covoiturage.common.exception.BusinessRuleException;
import ma.youcode.covoiturage.common.exception.DriverNotFoundException;
import ma.youcode.covoiturage.common.exception.ForbiddenActionException;
import ma.youcode.covoiturage.common.exception.NotYourVehicleException;
import ma.youcode.covoiturage.common.exception.TrajetNotFoundException;
import ma.youcode.covoiturage.common.exception.VehicleNotFoundException;
import ma.youcode.covoiturage.dto.CreateTrajetReq;
import ma.youcode.covoiturage.dto.FindTripDto;
import ma.youcode.covoiturage.dto.ReservationRes;
import ma.youcode.covoiturage.dto.TrajetDetailRes;
import ma.youcode.covoiturage.dto.TrajetSummaryRes;
import ma.youcode.covoiturage.entity.Driver;
import ma.youcode.covoiturage.entity.Reservation;
import ma.youcode.covoiturage.entity.StopPoint;
import ma.youcode.covoiturage.entity.Trajet;
import ma.youcode.covoiturage.entity.Vehicle;
import ma.youcode.covoiturage.entity.enums.StatutReservation;
import ma.youcode.covoiturage.entity.enums.StatutTrajet;
import ma.youcode.covoiturage.mapper.ReservationMapper;
import ma.youcode.covoiturage.mapper.TrajetMapper;
import ma.youcode.covoiturage.repository.DriverRepository;
import ma.youcode.covoiturage.repository.ReservationRepository;
import ma.youcode.covoiturage.repository.TrajetRepository;
import ma.youcode.covoiturage.repository.VehicleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TrajetServiceImp implements TrajetService {

    private final TrajetRepository trajetRepository;
    private final TrajetMapper trajetMapper;
    private final DriverRepository driverRepository;
    private final VehicleRepository vehicleRepository;
    private final ReservationRepository reservationRepository;
    private final ReservationMapper reservationMapper;

    public TrajetServiceImp(
            TrajetRepository trajetRepository,
            TrajetMapper trajetMapper,
            DriverRepository driverRepository,
            VehicleRepository vehicleRepository,
            ReservationRepository reservationRepository,
            ReservationMapper reservationMapper
    ) {
        this.trajetRepository = trajetRepository;
        this.trajetMapper = trajetMapper;
        this.driverRepository = driverRepository;
        this.vehicleRepository = vehicleRepository;
        this.reservationRepository = reservationRepository;
        this.reservationMapper = reservationMapper;
    }

    // DRIVER : publish with HIS vehicles only
    @Override
    public Trajet create(CreateTrajetReq req) {

        Driver driver = driverRepository.findById(req.driverId())
                .orElseThrow(() ->
                        new DriverNotFoundException(
                                "this driver with id " + req.driverId() + " not exist"
                        ));

        Vehicle vehicle = vehicleRepository.findById(req.vehiculeId())
                .orElseThrow(() ->
                        new VehicleNotFoundException(
                                "vehicle with id " + req.vehiculeId() + " not exists"
                        ));

        if (!vehicle.getDriver().getId().equals(driver.getId())) {
            throw new NotYourVehicleException("this vehicle is not yours");
        }

        Trajet trajet = trajetMapper.toEntity(req);
        trajet.setDriver(driver);
        trajet.setVehicle(vehicle);

        List<StopPoint> stops = new ArrayList<>(trajet.getStopPoints());
        trajet.getStopPoints().clear();

        for (int i = 0; i < stops.size(); i++) {
            stops.get(i).setSteps(i);
            trajet.addStopPoint(stops.get(i));
        }

        Trajet saved = trajetRepository.save(trajet);
        log.info("trajet {} published : driver {} , vehicle {} , {} -> {}",
                saved.getId(), driver.getId(), vehicle.getId(), saved.getStartVille(), saved.getEndVille());
        return saved;
    }

    // PASSENGER : search trajets
    @Transactional(readOnly = true)
    @Override
    public Page<FindTripDto> search(String startVille , String endVille , LocalDate date , BigDecimal prixMax , Integer nbPlaces , Pageable pageable) {
        // the order (by start date) is fixed inside the query, so we ignore any sort sent by the client
        Pageable page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize());

        return trajetRepository.searchBySegment(startVille, endVille, nbPlaces,
                date.atStartOfDay(), date.plusDays(1).atStartOfDay(), prixMax, page);
    }

    // ANYONE : consult a trajet
    @Transactional(readOnly = true)
    @Override
    public TrajetDetailRes findDetail(Long id) {
        Trajet trajet = trajetRepository.findDetailById(id).orElseThrow(() ->
                new TrajetNotFoundException("trajet with id " + id + " not exists"));

        Long placesRestantes = trajet.getPlaces() - reservationRepository.countConfirmedPlaces(id);
        return trajetMapper.toDetail(trajet, placesRestantes);
    }

    // DRIVER : consult HIS trajets
    @Transactional(readOnly = true)
    @Override
    public List<TrajetSummaryRes> findByDriver(Long driverId) {
        if (!driverRepository.existsById(driverId)) {
            throw new DriverNotFoundException("this driver with id " + driverId + " not exist");
        }
        return trajetRepository.findByDriverIdOrderByStartDateDesc(driverId).stream()
                .map(trajetMapper::toSummary)
                .toList();
    }

    // DRIVER : consult the reservations of HIS trajets only
    @Transactional(readOnly = true)
    @Override
    public List<ReservationRes> findReservations(Long trajetId , Long driverId) {
        Trajet trajet = trajetRepository.findById(trajetId).orElseThrow(() ->
                new TrajetNotFoundException("trajet with id " + trajetId + " not exists"));
        checkOwner(trajet, driverId);

        return reservationRepository.findAllByTrajetId(trajetId).stream()
                .map(reservationMapper::toRes)
                .toList();
    }

    // DRIVER : terminate HIS trajet, only after the departure time
    @Transactional
    @Override
    public void terminer(Long trajetId , Long driverId) {
        Trajet trajet = findForUpdate(trajetId);
        checkOwner(trajet, driverId);
        checkNotFinished(trajet);

        if (LocalDateTime.now().isBefore(trajet.getStartDate())) {
            throw new BusinessRuleException("a trajet can be terminated only after its departure time");
        }

        trajet.setStatutTrajet(StatutTrajet.TERMINE);
        trajetRepository.save(trajet);
        log.info("trajet {} terminated by driver {}", trajetId, driverId);
    }

    // DRIVER : cancel HIS trajet, all the confirmed reservations are cancelled in the same transaction
    @Transactional
    @Override
    public void annuler(Long trajetId , Long driverId) {
        Trajet trajet = findForUpdate(trajetId);
        checkOwner(trajet, driverId);
        checkNotFinished(trajet);

        List<Reservation> confirmed = reservationRepository.findByTrajetIdAndStatut(trajetId, StatutReservation.CONFIRMEE);
        confirmed.forEach(reservation -> reservation.setStatut(StatutReservation.ANNULEE));

        trajet.setStatutTrajet(StatutTrajet.ANNULE);
        trajetRepository.save(trajet);
        log.info("trajet {} cancelled by driver {} , {} reservation(s) cancelled", trajetId, driverId, confirmed.size());
    }

    private Trajet findForUpdate(Long trajetId) {
        return trajetRepository.findByIdForUpdate(trajetId).orElseThrow(() ->
                new TrajetNotFoundException("trajet with id " + trajetId + " not exists"));
    }

    private void checkOwner(Trajet trajet , Long driverId) {
        if (!trajet.getDriver().getId().equals(driverId)) {
            throw new ForbiddenActionException("this trajet is not yours");
        }
    }

    private void checkNotFinished(Trajet trajet) {
        if (trajet.getStatutTrajet() == StatutTrajet.TERMINE || trajet.getStatutTrajet() == StatutTrajet.ANNULE) {
            throw new BusinessRuleException("this trajet is already " + trajet.getStatutTrajet() + " , no change is possible");
        }
    }
}
