package project.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import project.validation.CardValidation;

public class CardValidator implements ConstraintValidator<CardValidation,Integer> {
    @Override
    public boolean isValid(Integer integer, ConstraintValidatorContext constraintValidatorContext) {
        return integer>10000000;
    }
}
