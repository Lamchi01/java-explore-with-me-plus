package ewm.error;

import ewm.exception.EntityNotFoundException;
import ewm.exception.EntityUpdateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class ErrorHandlerControllerAdvice {

    @ExceptionHandler({EntityNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError onEntityNotFoundException(final EntityNotFoundException e) {
        log.error("EntityNotFoundException - 404: {}", e.getMessage(), e);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();
        return new ApiError("NOT_FOUND", "entity not found", stackTrace, LocalDateTime.now().toString());
    }

    @ExceptionHandler({EntityUpdateException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ApiError onEntityUpdateException(final EntityUpdateException e) {
        log.error("EntityUpdateException - 409: {}", e.getMessage(), e);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();
        return new ApiError("FORBIDDEN", "entity update forbidden", stackTrace, LocalDateTime.now().toString());
    }

    @ExceptionHandler({DataIntegrityViolationException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError onDataIntegrityViolationException(final DataIntegrityViolationException e) {
        log.error("DataIntegrityViolationException - 409: {}", e.getMessage(), e);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();
        return new ApiError("CONFLICT", "Integrity constraint has been violated", stackTrace, LocalDateTime.now().toString());
    }

    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleAnyException(final Throwable e) {
        log.error("Error:500; {}", e.getMessage(), e);
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String stackTrace = sw.toString();

        return new ApiError("INTERNAL_SERVER_ERROR", "internal server error", stackTrace, LocalDateTime.now().toString());
    }

    public record ApiError(String status, String reason, String message, String timestamp) {
    }
}