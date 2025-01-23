package ewm.error;

public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class<?> entityClass, String message) {
        super(entityClass.getSimpleName() + message);
    }
}