package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.dto.CreatePassengerReq;
import ma.youcode.covoiturage.entity.Passenger;
import ma.youcode.covoiturage.mapper.PassengerMapper;
import ma.youcode.covoiturage.repository.PassengerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PassengerServiceImpTest {
    @Mock
    PassengerRepository passengerRepository;

    @Mock
    PassengerMapper passengerMapper;

    @InjectMocks
    PassengerServiceImp passengerService;

    @Test
    void shouldCreatePassenger(){
        //arrange
        Passenger passenger = new Passenger();
        CreatePassengerReq req = new CreatePassengerReq(
                "test",
                "bentest",
                "test@gmail.com",
                "+20215485");
        when(passengerRepository.save(passenger)).thenReturn(passenger);
        when(passengerMapper.toEntity(req)).thenReturn(passenger);
        //act

        Passenger pass = passengerService.createPassenger(req);

        //assert
        assertThat(pass).isEqualTo(passenger);

        verify(passengerMapper).toEntity(req);
        verify(passengerRepository).save(passenger);

    }




}