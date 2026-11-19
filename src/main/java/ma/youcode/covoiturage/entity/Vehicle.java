package ma.youcode.covoiturage.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.youcode.covoiturage.dto.ModifierVehicleReq;

@Entity
@Getter
@Setter
@NoArgsConstructor
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
    Integer capacite;

            @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "driver_id",nullable = true)
    Driver driver;


}
