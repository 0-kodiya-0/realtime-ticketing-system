package org.example.opp_cw.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.bson.types.ObjectId;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsObjectIdValidator.class)
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsObjectIdValid {
    String message() default "invalid object id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class IsObjectIdValidator implements ConstraintValidator<IsObjectIdValid, String> {

    @Override
    public boolean isValid(String str, ConstraintValidatorContext constraintValidatorContext) {
        return ObjectId.isValid(str);
    }
}
