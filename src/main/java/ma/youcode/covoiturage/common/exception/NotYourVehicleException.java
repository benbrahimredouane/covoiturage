package ma.youcode.covoiturage.common.exception;

public class NotYourVehicleException extends RuntimeException {
    public NotYourVehicleException(String message) {
        super(message);
    }
}
