package ma.youcode.covoiturage.common.exception;

public class TrajetNotFoundException extends RuntimeException {
    public TrajetNotFoundException(String message) {
        super(message);
    }
}
