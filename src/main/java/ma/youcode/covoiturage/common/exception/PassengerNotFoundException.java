package ma.youcode.covoiturage.common.exception;

public class PassengerNotFoundException extends ResourceNotFoundException {
    public PassengerNotFoundException(String message) {
        super(message);
    }
}
