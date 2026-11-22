package ma.youcode.covoiturage.repository;

import jakarta.persistence.LockModeType;
import ma.youcode.covoiturage.dto.FindTripDto;
import ma.youcode.covoiturage.entity.Trajet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface TrajetRepository extends JpaRepository<Trajet, Long>, JpaSpecificationExecutor<Trajet> {

    @Query(value = """
    select new ma.youcode.covoiturage.dto.FindTripDto(
        t.id, depart.ville, arrivee.ville, depart.heurePassage, arrivee.heurePassage,
        arrivee.prixDepuitsDepart - depart.prixDepuitsDepart,
        t.places - (select coalesce(sum(r.nbPlaces), 0L) from Reservation r
                    where r.trajet = t
                      and r.statut = ma.youcode.covoiturage.entity.enums.StatutReservation.CONFIRMEE))
    from Trajet t
    join t.stopPoints depart
    join t.stopPoints arrivee
    where t.statutTrajet = ma.youcode.covoiturage.entity.enums.StatutTrajet.PUBLIE
      and lower(depart.ville) = lower(:startVille)
      and lower(arrivee.ville) = lower(:endVille)
      and depart.steps < arrivee.steps
      and t.startDate >= :dateDebut and t.startDate < :dateFin
      and t.places - (select coalesce(sum(r.nbPlaces), 0L) from Reservation r
                      where r.trajet = t
                        and r.statut = ma.youcode.covoiturage.entity.enums.StatutReservation.CONFIRMEE) >= :nbPlaces
      and (:prixMax is null or arrivee.prixDepuitsDepart - depart.prixDepuitsDepart <= :prixMax)
    order by t.startDate asc
    """,
            countQuery = """
    select count(t)
    from Trajet t
    join t.stopPoints depart
    join t.stopPoints arrivee
    where t.statutTrajet = ma.youcode.covoiturage.entity.enums.StatutTrajet.PUBLIE
      and lower(depart.ville) = lower(:startVille)
      and lower(arrivee.ville) = lower(:endVille)
      and depart.steps < arrivee.steps
      and t.startDate >= :dateDebut and t.startDate < :dateFin
      and t.places - (select coalesce(sum(r.nbPlaces), 0L) from Reservation r
                      where r.trajet = t
                        and r.statut = ma.youcode.covoiturage.entity.enums.StatutReservation.CONFIRMEE) >= :nbPlaces
      and (:prixMax is null or arrivee.prixDepuitsDepart - depart.prixDepuitsDepart <= :prixMax)
    """)
    Page<FindTripDto> searchBySegment(@Param("startVille") String startVille,
                                      @Param("endVille") String endVille,
                                      @Param("nbPlaces") Integer nbPlaces,
                                      @Param("dateDebut") LocalDateTime dateDebut,
                                      @Param("dateFin") LocalDateTime dateFin,
                                      @Param("prixMax") BigDecimal prixMax,
                                      Pageable pageable);

    // locks the trajet row until the end of the transaction : two reservations can't take the same last place
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select t from Trajet t where t.id = :id")
    Optional<Trajet> findByIdForUpdate(@Param("id") Long id);

    // detail of a trajet in one query (no N+1)
    @Query("""
    select  t from Trajet t
    join fetch t.driver
    join fetch t.vehicle
    left join fetch t.stopPoints
    where t.id = :id
    """)
    Optional<Trajet> findDetailById(@Param("id") Long id);

    List<Trajet> findByDriverIdOrderByStartDateDesc(Long driverId);

    boolean existsByVehicleId(Long vehicleId);
}
