package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.dto.CreatePassengerReq;
import ma.youcode.covoiturage.dto.CreatePassengerRes;
import ma.youcode.covoiturage.entity.Passenger;

public interface PassengerService {
    public Passenger createPassenger(CreatePassengerReq req);
}
