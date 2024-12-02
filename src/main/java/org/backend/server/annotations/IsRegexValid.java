package org.backend.server.annotations;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

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

class IsRegexValidator implements ConstraintValidator<IsRegexValid, String> {

    String regexp;
    boolean isNullable;
    String isNullMessage = "must not be null";

    @Override
    public void initialize(IsRegexValid constraintAnnotation) {
        regexp = constraintAnnotation.regexp();
        isNullable = constraintAnnotation.isNullable();
    }

    private void reBuildConstraintValidatorContext(ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(isNullMessage).addConstraintViolation();
    }

    @Override
    public boolean isValid(String strValue, ConstraintValidatorContext context) {
        if (isNullable) {
            reBuildConstraintValidatorContext(context);
            return strValue == null || strValue.matches(regexp);
        }
        if (strValue == null) {
            reBuildConstraintValidatorContext(context);
            return false;
        }
        return !(strValue.isBlank()) && strValue.matches(regexp);
    }
}