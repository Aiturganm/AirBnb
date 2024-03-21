package project.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import project.validation.PhoneNumberValidation;

public class PhoneNumberValidator implements ConstraintValidator<PhoneNumberValidation, String> {
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext context) {
        return phoneNumber.startsWith("+996") && phoneNumber.length() == 13;
    }
}
