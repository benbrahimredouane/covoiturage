package ma.youcode.covoiturage.service;

import ma.youcode.covoiturage.common.exception.BusinessRuleException;
import ma.youcode.covoiturage.common.exception.ForbiddenActionException;
import ma.youcode.covoiturage.common.exception.PassengerNotFoundException;
import ma.youcode.covoiturage.common.exception.ReservationNotFoundException;
import ma.youcode.covoiturage.dto.ReservationRes;
import ma.youcode.covoiturage.entity.Passenger;
import ma.youcode.covoiturage.entity.Reservation;
import ma.youcode.covoiturage.entity.Trajet;
import ma.youcode.covoiturage.entity.enums.StatutReservation;
import ma.youcode.covoiturage.entity.enums.StatutTrajet;
import ma.youcode.covoiturage.mapper.ReservationMapper;
import ma.youcode.covoiturage.repository.PassengerRepository;
import ma.youcode.covoiturage.repository.ReservationRepository;
import ma.youcode.covoiturage.repository.TrajetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImpTest {

    @Mock
    private ReservationRepository reservationRepository;

    @Mock
    private TrajetRepository trajetRepository;

    @Mock
    private PassengerRepository passengerRepository;

    @Mock
    private ReservationMapper reservationMapper;

    @InjectMocks
    private ReservationServiceImp reservationService;

    // ---------- helpers
    private Trajet trajet(StatutTrajet statut , LocalDateTime startDate){
        Trajet trajet = new Trajet();
        trajet.setId(10L);
        trajet.setStatutTrajet(statut);
        trajet.setStartDate(startDate);
        return trajet;
    }

    private Reservation reservation(Long passengerId , Trajet trajet , StatutReservation statut){
        Passenger passenger = new Passenger();
        passenger.setId(passengerId);

        Reservation reservation = new Reservation();
        reservation.setId(100L);
        reservation.setPassenger(passenger);
        reservation.setTrajet(trajet);
        reservation.setNbPlaces(2);
        reservation.setStatut(statut);
        return reservation;
    }

    private ReservationRes res(Long id){
        return new ReservationRes(id, 1L, "Salma", "Idrissi", 10L, "Settat", "Marrakech", 1,
                new BigDecimal("50.00"), StatutReservation.CONFIRMEE, LocalDateTime.now());
    }

    // ---------- cancel : nominal cases
    @Test
    void shouldCancelReservationAndReopenTheTrajetWhenItWasComplet(){
        //arrange
        Trajet trajet = trajet(StatutTrajet.COMPLET, LocalDateTime.now().plusDays(3));
        Reservation reservation = reservation(1L, trajet, StatutReservation.CONFIRMEE);

        when(reservationRepository.findById(100L)).thenReturn(Optional.of(reservation));
        when(trajetRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(trajet));

        //act
        reservationService.cancel(100L, 1L);

        //assert
        assertThat(reservation.getStatut()).isEqualTo(StatutReservation.ANNULEE);
        assertThat(trajet.getStatutTrajet()).isEqualTo(StatutTrajet.PUBLIE);

        verify(reservationRepository).save(reservation);
        verify(trajetRepository).save(trajet);
    }

    @Test
    void shouldCancelReservationAndLeaveThePublieTrajetUntouched(){
        //arrange
        Trajet trajet = trajet(StatutTrajet.PUBLIE, LocalDateTime.now().plusDays(3));
        Reservation reservation = reservation(1L, trajet, StatutReservation.CONFIRMEE);

        when(reservationRepository.findById(100L)).thenReturn(Optional.of(reservation));
        when(trajetRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(trajet));

        //act
        reservationService.cancel(100L, 1L);

        //assert
        assertThat(reservation.getStatut()).isEqualTo(StatutReservation.ANNULEE);
        assertThat(trajet.getStatutTrajet()).isEqualTo(StatutTrajet.PUBLIE);

        verify(reservationRepository).save(reservation);
        verify(trajetRepository, never()).save(any());
    }

    // ---------- cancel : refused rules
    @Test
    void shouldRefuseCancellationLessThan24HoursBeforeTheDeparture(){
        //arrange
        Trajet trajet = trajet(StatutTrajet.COMPLET, LocalDateTime.now().plusHours(2));
        Reservation reservation = reservation(1L, trajet, StatutReservation.CONFIRMEE);

        when(reservationRepository.findById(100L)).thenReturn(Optional.of(reservation));
        when(trajetRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(trajet));

        //act + assert
        assertThatThrownBy(() -> reservationService.cancel(100L, 1L))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("24 hours");

        assertThat(reservation.getStatut()).isEqualTo(StatutReservation.CONFIRMEE);
        assertThat(trajet.getStatutTrajet()).isEqualTo(StatutTrajet.COMPLET);
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldRefuseCancellationOfTheReservationOfAnotherPassenger(){
        //arrange
        Trajet trajet = trajet(StatutTrajet.PUBLIE, LocalDateTime.now().plusDays(3));
        Reservation reservation = reservation(1L, trajet, StatutReservation.CONFIRMEE);

        when(reservationRepository.findById(100L)).thenReturn(Optional.of(reservation));

        //act + assert : passenger 2 tries to cancel the reservation of passenger 1
        assertThatThrownBy(() -> reservationService.cancel(100L, 2L))
                .isInstanceOf(ForbiddenActionException.class);

        assertThat(reservation.getStatut()).isEqualTo(StatutReservation.CONFIRMEE);
        verify(trajetRepository, never()).findByIdForUpdate(any());
        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldRefuseCancellationOfAnAlreadyCancelledReservation(){
        //arrange
        Trajet trajet = trajet(StatutTrajet.PUBLIE, LocalDateTime.now().plusDays(3));
        Reservation reservation = reservation(1L, trajet, StatutReservation.ANNULEE);

        when(reservationRepository.findById(100L)).thenReturn(Optional.of(reservation));
        when(trajetRepository.findByIdForUpdate(10L)).thenReturn(Optional.of(trajet));

        //act + assert
        assertThatThrownBy(() -> reservationService.cancel(100L, 1L))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("already cancelled");

        verify(reservationRepository, never()).save(any());
    }

    @Test
    void shouldRefuseCancellationWhenTheReservationDoesNotExist(){
        //arrange
        when(reservationRepository.findById(100L)).thenReturn(Optional.empty());

        //act + assert
        assertThatThrownBy(() -> reservationService.cancel(100L, 1L))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    // ---------- history
    @Test
    void shouldReturnTheReservationsOfThePassenger(){
        //arrange
        Trajet trajet = trajet(StatutTrajet.PUBLIE, LocalDateTime.now().plusDays(3));
        Reservation first = reservation(1L, trajet, StatutReservation.CONFIRMEE);
        Reservation second = reservation(1L, trajet, StatutReservation.ANNULEE);

        when(passengerRepository.existsById(1L)).thenReturn(true);
        when(reservationRepository.findAllByPassengerId(1L)).thenReturn(List.of(first, second));
        when(reservationMapper.toRes(first)).thenReturn(res(1L));
        when(reservationMapper.toRes(second)).thenReturn(res(2L));

        //act
        List<ReservationRes> history = reservationService.findByPassenger(1L);

        //assert
        assertThat(history).hasSize(2);
        assertThat(history).extracting(ReservationRes::id).containsExactly(1L, 2L);
    }

    @Test
    void shouldRefuseTheHistoryOfAnUnknownPassenger(){
        //arrange
        when(passengerRepository.existsById(9L)).thenReturn(false);

        //act + assert
        assertThatThrownBy(() -> reservationService.findByPassenger(9L))
                .isInstanceOf(PassengerNotFoundException.class);

        verify(reservationRepository, never()).findAllByPassengerId(any());
    }

    @Test
    void shouldReturnOneReservationToItsOwner(){
        //arrange
        Trajet trajet = trajet(StatutTrajet.PUBLIE, LocalDateTime.now().plusDays(3));
        Reservation reservation = reservation(1L, trajet, StatutReservation.CONFIRMEE);

        when(reservationRepository.findById(100L)).thenReturn(Optional.of(reservation));
        when(reservationMapper.toRes(reservation)).thenReturn(res(100L));

        //act
        ReservationRes result = reservationService.findById(100L, 1L);

        //assert
        assertThat(result.id()).isEqualTo(100L);
    }

    @Test
    void shouldRefuseToShowTheReservationOfAnotherPassenger(){
        //arrange
        Trajet trajet = trajet(StatutTrajet.PUBLIE, LocalDateTime.now().plusDays(3));
        Reservation reservation = reservation(1L, trajet, StatutReservation.CONFIRMEE);

        when(reservationRepository.findById(100L)).thenReturn(Optional.of(reservation));

        //act + assert
        assertThatThrownBy(() -> reservationService.findById(100L, 2L))
                .isInstanceOf(ForbiddenActionException.class);

        verify(reservationMapper, never()).toRes(any());
    }
}