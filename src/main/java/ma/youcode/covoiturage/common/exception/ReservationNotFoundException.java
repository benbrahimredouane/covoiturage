package ma.youcode.covoiturage.common.exception;

public class ReservationNotFoundException extends ResourceNotFoundException {
    public ReservationNotFoundException(String message) {
        super(message);
    }
}
