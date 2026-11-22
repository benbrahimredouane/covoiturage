package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.dto.CreateTrajetReq;
import ma.youcode.covoiturage.dto.CreateTrajetRes;
import ma.youcode.covoiturage.dto.FindTripDto;
import ma.youcode.covoiturage.entity.Trajet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.time.LocalDateTime;

public interface TrajetService {
    public Trajet create(CreateTrajetReq req);
    public Page<FindTripDto> search(String startVille , String endVille , LocalDateTime dateTime , Pageable pageable);
}
