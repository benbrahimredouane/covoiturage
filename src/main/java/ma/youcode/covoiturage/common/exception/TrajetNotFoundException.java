package ma.youcode.covoiturage.common.exception;

public class TrajetNotFoundException extends ResourceNotFoundException {
    public TrajetNotFoundException(String message) {
        super(message);
    }
}
