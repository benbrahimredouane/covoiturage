package ma.youcode.covoiturage.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Passenger {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false)
    String nom;

    @Column(nullable = false)
    String prenom;


    @Column(nullable = false ,unique = true)
    String email;

    @Column(nullable = false)
    String phoneNumber;
}
