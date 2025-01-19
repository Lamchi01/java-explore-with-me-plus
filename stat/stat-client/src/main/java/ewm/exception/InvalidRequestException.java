package ewm.exception;


public class InvalidRequestException extends RuntimeException {
    public InvalidRequestException(String message) {
        super(message);
    }

    public InvalidRequestException(Class<?> entityClass, String message) {
        super(entityClass.getSimpleName() + message);
    }

    public InvalidRequestException(Class<?> entityClass, int statusCode, String message) {
        super(entityClass.getSimpleName() + statusCode + message);
    }

}
