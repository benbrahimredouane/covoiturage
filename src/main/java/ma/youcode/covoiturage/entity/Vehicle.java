package ma.youcode.covoiturage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ma.youcode.covoiturage.dto.ModifierVehicleReq;

import java.util.ArrayList;
import java.util.List;

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
            @OneToMany(mappedBy = "vehicle")
    @JsonIgnore
    List<Trajet> trajets = new ArrayList<>();


}
