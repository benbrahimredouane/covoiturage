package ma.youcode.covoiturage.repository;

import ma.youcode.covoiturage.entity.Reservation;
import ma.youcode.covoiturage.entity.enums.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReservationRepository extends JpaRepository<Reservation , Long> {

    boolean existsByPassengerIdAndTrajetIdAndStatut(Long passengerId , Long trajetId , StatutReservation statut);

    // places restantes = trajet.places - this result (calculated, never stored)
    @Query("""
            select coalesce(sum(r.nbPlaces), 0L)
            from Reservation r
            where r.trajet.id = :trajetId
              and r.statut = ma.youcode.covoiturage.entity.enums.StatutReservation.CONFIRMEE
            """)
    Long countConfirmedPlaces(@Param("trajetId") Long trajetId);
}