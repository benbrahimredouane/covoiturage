package ma.youcode.covoiturage.dto;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateDriverReq(

        @NotBlank(message = "nom must not be blank")
        @Size(max = 50 , message = "name is so long")
        String nom,

        @NotBlank(message = "prenom must not be blank")
        @Size(max = 50 , message = "prenom so long")
        String prenom,

        @NotBlank(message = "email must not be blank")
        @Email(message = "email invalid")
        @Size(max = 200 , message = "try shorter email")
        String email,

        @NotBlank(message = "phone number must not be blank")
        @Size(min = 8 , max = 14 , message = "try valid number")
        String phoneNumber,

        @NotBlank(message = "permis number must not be blank")
        String permisN

) { }
