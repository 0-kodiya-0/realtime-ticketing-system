package org.backend.server.annotations;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class IsRegexValidator implements ConstraintValidator<IsRegexValid, String> {

    private String regexp;
    private boolean isNullable;
    private String isNullMessage = "must not be null";

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