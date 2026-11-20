package ma.youcode.covoiturage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Setter
@Getter
public class StopPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;


    @Column(nullable = false)
    private String ville;

    @Column(nullable = false)
    private LocalDateTime heurePassage;

    @Column(nullable = false)
    private BigDecimal prixDepuitsDepart;

    @Column(nullable = false)
    private Integer steps;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trajet_id" , nullable = false)
    private Trajet trajet;

}
