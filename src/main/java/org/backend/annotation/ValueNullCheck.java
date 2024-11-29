package org.backend.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.time.LocalDate;
import java.util.List;

@Documented
@Constraint(validatedBy = {IsListValueNullValidator.class, IsLocalDateValueNullValidator.class})
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueNullCheck {
    boolean isNullable() default true;

    String message() default "must be null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class IsListValueNullValidator implements ConstraintValidator<ValueNullCheck, List<?>> {

    boolean isNullable;
    String notNullableMessage = "list must not be null";

    @Override
    public void initialize(ValueNullCheck constraintAnnotation) {
        isNullable = constraintAnnotation.isNullable();
    }

    @Override
    public boolean isValid(List<?> secureCode, ConstraintValidatorContext context) {
        if (isNullable) {
            return secureCode == null;
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(notNullableMessage).addConstraintViolation();
            return secureCode != null;
        }
    }
}

class IsLocalDateValueNullValidator implements ConstraintValidator<ValueNullCheck, LocalDate> {

    boolean isNullable;
    String notNullableMessage = "local date must not be null";

    @Override
    public void initialize(ValueNullCheck constraintAnnotation) {
        isNullable = constraintAnnotation.isNullable();
    }

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
        if (isNullable) {
            return localDate == null;
        } else {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(notNullableMessage).addConstraintViolation();
            return localDate != null;
        }
    }
}
