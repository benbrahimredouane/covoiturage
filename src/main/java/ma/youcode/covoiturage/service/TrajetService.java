package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.dto.CreateTrajetReq;
import ma.youcode.covoiturage.dto.FindTripDto;
import ma.youcode.covoiturage.dto.ReservationRes;
import ma.youcode.covoiturage.dto.TrajetDetailRes;
import ma.youcode.covoiturage.dto.TrajetSummaryRes;
import ma.youcode.covoiturage.entity.Trajet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public interface TrajetService {
    public Trajet create(CreateTrajetReq req);
    public Page<FindTripDto> search(String startVille , String endVille , LocalDate date , BigDecimal prixMax , Integer nbPlaces , Pageable pageable);
    public TrajetDetailRes findDetail(Long id);
    public List<TrajetSummaryRes> findByDriver(Long driverId);
    public List<ReservationRes> findReservations(Long trajetId , Long driverId);
    public void terminer(Long trajetId , Long driverId);
    public void annuler(Long trajetId , Long driverId);
}
