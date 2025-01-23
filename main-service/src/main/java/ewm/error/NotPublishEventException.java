package ewm.error;

public class NotPublishEventException extends RuntimeException {
    public NotPublishEventException(String message) {
        super(message);
    }
}