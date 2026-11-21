
package ma.youcode.covoiturage.repository;

import ma.youcode.covoiturage.entity.Trajet;
import ma.youcode.covoiturage.entity.enums.StatutTrajet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;

public interface TrajetRepository extends JpaRepository<Trajet, Long>, JpaSpecificationExecutor<Trajet> {

    @Query("""
        select t
        from Trajet t
        join t.stopPoints depart
        join t.stopPoints arrivee
        where depart.ville = :startVille
          and arrivee.ville = :endVille
          and depart.steps < arrivee.steps
          and t.statutTrajet = :statut
          and t.startDate >= :dateDebut
          and t.startDate < :dateFin
        order by depart.heurePassage asc
        """)
    Page<Trajet> searchBySegment(
            @Param("startVille") String startVille,
            @Param("endVille") String endVille,
            @Param("statut") StatutTrajet statut,
            @Param("dateDebut") LocalDateTime dateDebut,
            @Param("dateFin") LocalDateTime dateFin,
            Pageable pageable
    );
    Optional<Trajet> findByIdForUpdate(Long id);
}
