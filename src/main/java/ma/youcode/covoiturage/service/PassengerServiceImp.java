package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.dto.CreatePassengerReq;
import ma.youcode.covoiturage.dto.CreatePassengerRes;
import ma.youcode.covoiturage.entity.Passenger;
import ma.youcode.covoiturage.mapper.PassengerMapper;
import ma.youcode.covoiturage.repository.PassengerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PassengerServiceImp implements PassengerService{
    private final PassengerRepository passengerRepository;
    private final PassengerMapper passengerMapper;
    public PassengerServiceImp(PassengerRepository passengerRepository,PassengerMapper passengerMapper ) {
        this.passengerRepository = passengerRepository;
        this.passengerMapper = passengerMapper;
    }


    @Transactional
    public Passenger createPassenger(CreatePassengerReq req){
        Passenger passenger = passengerMapper.toEntity(req);
        return passengerRepository.save(passenger);


    }
}
