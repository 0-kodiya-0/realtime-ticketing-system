package org.backend.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import org.bson.types.ObjectId;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsObjectIdValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsObjectIdValid {
    String message() default "invalid object id";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class IsObjectIdValidator implements ConstraintValidator<IsObjectIdValid, ObjectId> {

    @Override
    public boolean isValid(ObjectId objIdValue, ConstraintValidatorContext context) {
        return objIdValue != null && ObjectId.isValid(objIdValue.toHexString());
    }
}
