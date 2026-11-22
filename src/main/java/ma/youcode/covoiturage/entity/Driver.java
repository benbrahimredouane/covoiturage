package ma.youcode.covoiturage.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class Driver {

//    id, nom, prénom, email, téléphone, numéro de permis

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    @Column(nullable = false)
    String nom;

    @Column(nullable = false)
    String prenom;

    @Column(nullable = false ,unique = true)
    String email;

    @Column(nullable = false)
    String phoneNumber;

    @Column(nullable = false,unique = true)
    String permisN;


    @OneToMany(mappedBy = "driver")
            @JsonIgnore
    List<Vehicle> vehicles = new ArrayList<>();



}
