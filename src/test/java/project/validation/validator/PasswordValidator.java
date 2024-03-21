package project.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import project.validation.PasswordValidation;

public class PasswordValidator implements ConstraintValidator<PasswordValidation, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.length() >= 4;
    }
}
