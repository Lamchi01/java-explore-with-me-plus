package ewm.error;

public class RepeatUserRequestorException extends RuntimeException {
    public RepeatUserRequestorException(String message) {
        super(message);
    }
}