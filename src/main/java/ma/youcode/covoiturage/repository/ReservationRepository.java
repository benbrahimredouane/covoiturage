package ma.youcode.covoiturage.repository;

import ma.youcode.covoiturage.entity.Reservation;
import ma.youcode.covoiturage.entity.enums.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation , Long> {

    List<Reservation> findByTrajetIdAndStatut(Long trajetId , StatutReservation statut);

    @Query("""
            select r from Reservation r
            join fetch r.passenger join fetch r.trajet join fetch r.pointMontee join fetch r.pointDescente
            where r.trajet.id = :trajetId
            order by r.dateReservation desc
            """)
    List<Reservation> findAllByTrajetId(@Param("trajetId") Long trajetId);

    @Query("""
            select r from Reservation r
            join fetch r.passenger join fetch r.trajet join fetch r.pointMontee join fetch r.pointDescente
            where r.passenger.id = :passengerId
            order by r.dateReservation desc
            """)
    List<Reservation> findAllByPassengerId(@Param("passengerId") Long passengerId);

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