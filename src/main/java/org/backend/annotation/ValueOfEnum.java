package org.backend.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;

import java.lang.annotation.*;
import java.util.Arrays;

@Documented
@Constraint(validatedBy = ValueOfEnumValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.LOCAL_VARIABLE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValueOfEnum {
    Class<? extends Enum<?>> enumClass();

    String message() default "must be one of the predefined values";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

class ValueOfEnumValidator implements ConstraintValidator<ValueOfEnum, String> {

    private String[] acceptedValues;
    private String customMessage;

    @Override
    public void initialize(ValueOfEnum annotation) {
        acceptedValues = Arrays.stream(annotation.enumClass().getEnumConstants())
                .map(Enum::name)
                .toArray(String[]::new);
        customMessage = "must be one of the following values: " + Arrays.toString(acceptedValues);
    }

    @Override
    public boolean isValid(String strValue, ConstraintValidatorContext context) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(customMessage).addConstraintViolation();
        return strValue != null && !(strValue.isBlank()) && Arrays.asList(acceptedValues).contains(strValue);
    }
}