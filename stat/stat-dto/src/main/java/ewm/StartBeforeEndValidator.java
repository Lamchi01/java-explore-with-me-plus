package ewm;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, StartEnd> {

    @Override
    public void initialize(final StartBeforeEnd constraintAnnotation) {
    }

    @Override
    public boolean isValid(final StartEnd value, ConstraintValidatorContext context) {

        final LocalDateTime start = value.getStart();
        final LocalDateTime end = value.getEnd();

        return start != null && end != null && !start.isAfter(end) && !start.isEqual(end);
    }
}
