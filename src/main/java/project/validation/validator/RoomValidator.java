package project.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import project.validation.RoomValidation;

public class RoomValidator implements ConstraintValidator<RoomValidation, Byte> {
    @Override
    public boolean isValid(Byte value, ConstraintValidatorContext context) {
        return value > 0;
    }
}
