package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.dto.CreateTrajetReq;
import ma.youcode.covoiturage.entity.Trajet;

public interface TrajetService {
    public Trajet create(CreateTrajetReq req);
}
