package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.dto.CreateReservationReq;
import ma.youcode.covoiturage.dto.CreateReservationRes;
import ma.youcode.covoiturage.dto.ReservationRes;

import java.util.List;

public interface ReservationService {
    public CreateReservationRes reserve(CreateReservationReq req);
    public ReservationRes findById(Long id , Long passengerId);
    public List<ReservationRes> findByPassenger(Long passengerId);
    public void cancel(Long id , Long passengerId);
}
