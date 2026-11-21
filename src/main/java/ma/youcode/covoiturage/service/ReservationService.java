package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.dto.CreateReservationReq;
import ma.youcode.covoiturage.dto.CreateReservationRes;

public interface ReservationService {
    public CreateReservationRes reserve(CreateReservationReq req);
}