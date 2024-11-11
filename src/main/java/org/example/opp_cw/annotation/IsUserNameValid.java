package org.example.opp_cw.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsUserNameValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsUserNameValid {
    String message() default "username is not valid";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class IsUserNameValidator implements ConstraintValidator<IsUserNameValid, String> {

    @Override
    public boolean isValid(String strValue, ConstraintValidatorContext context) {
        return strValue != null && !(strValue.isBlank()) && strValue.matches("^[a-zA-Z0-9@_*.]+$") && strValue.length() >= 8 && strValue.length() <= 16;
    }
}
