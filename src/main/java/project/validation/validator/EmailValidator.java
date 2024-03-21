package project.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import project.validation.EmailValidation;

public class EmailValidator implements ConstraintValidator<EmailValidation, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value.contains("@gmail.com");
    }
}
