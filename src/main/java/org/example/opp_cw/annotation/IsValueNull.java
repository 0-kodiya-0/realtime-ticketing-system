package org.example.opp_cw.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.example.opp_cw.dto.RandomSecureCode;

import java.lang.annotation.*;
import java.util.List;

@Documented
@Constraint(validatedBy = IsValueNullValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsValueNull {
    String message() default "must be null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class IsValueNullValidator implements ConstraintValidator<IsValueNull, List<RandomSecureCode>> {

    @Override
    public boolean isValid(List<RandomSecureCode> secureCode, ConstraintValidatorContext constraintValidatorContext) {
        return secureCode == null;
    }
}
