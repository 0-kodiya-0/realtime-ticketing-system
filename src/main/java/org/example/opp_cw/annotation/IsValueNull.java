package org.example.opp_cw.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

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

class IsValueNullValidator implements ConstraintValidator<IsValueNull, List<Integer>> {

    @Override
    public boolean isValid(List<Integer> integers, ConstraintValidatorContext constraintValidatorContext) {
        return integers == null;
    }
}
