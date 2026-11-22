package ma.youcode.covoiturage.common.exception;

public class VehicleNotFoundException extends ResourceNotFoundException {
    public VehicleNotFoundException(String message) {
        super(message);
    }
}
