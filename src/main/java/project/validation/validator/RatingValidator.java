package project.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import project.validation.RatingValidation;
import project.validation.RoomValidation;

public class RatingValidator implements ConstraintValidator<RatingValidation, Byte> {
    @Override
    public boolean isValid(Byte value, ConstraintValidatorContext context) {
       return value >= 0 && value <= 5;
    }
   }
