package org.example.opp_cw.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsUserNameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsUserNameValid {
    String message() default "username is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class IsUserNameValidator implements ConstraintValidator<IsUserNameValid, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return s != null && !(s.isBlank()) && s.matches("^[a-zA-Z0-9@_*.]+$") && s.length() >= 8 && s.length() <= 16;
    }
}
