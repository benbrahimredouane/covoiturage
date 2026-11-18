package ma.youcode.covoiturage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false )
    String marque;
    @Column(nullable = false )
    String modele;
    @Column(nullable = false , unique = true )
    String immatriculation;
    @Column(nullable = false )
    int capacite;

            @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "driver_id",nullable = false)
    Driver driver;
}
