package org.backend.server.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.NoArgsConstructor;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = IsRegexValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface IsRegexValid {
    String regexp();

    boolean isNullable() default false;

    String message() default "double-check the format";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}