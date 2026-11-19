package ma.youcode.covoiturage.entity;

import jakarta.persistence.*;
import ma.youcode.covoiturage.entity.enums.StatutTrajet;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Trajet {
    @Id
            @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;

    @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "driver_id",nullable = false)
    Driver driver;

    @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "vehicle_id",nullable = false)
    Vehicle vehicle;
    @Column(nullable = false)
    String startVille;
    @Column(nullable = false)
    String endVille;

    LocalDateTime startDate;
    @Column(nullable = false)
    Integer places;
    @Enumerated(EnumType.STRING)
    StatutTrajet statutTrajet = StatutTrajet.PUBLIE;

    @OneToMany(mappedBy = "trajet",
            cascade = CascadeType.ALL ,
            orphanRemoval = true)
    @OrderBy("order ASC ")
    private List<StopPoint> stopPoints = new ArrayList<>();

    public void addStopPoint(StopPoint stopPoint){
        stopPoints.add(stopPoint);
        stopPoint.setTrajet(this);
    }



}
