package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.dto.CreateDriverReq;
import ma.youcode.covoiturage.entity.Driver;

public interface DriverService {
    public Driver createDriver(CreateDriverReq req);
}
