package project.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import project.validation.validator.RatingValidator;
import project.validation.validator.RoomValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.ElementType.TYPE_USE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = {RatingValidator.class})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface RatingValidation {
    String message() default "rating must be not more then 5 and not negative";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}