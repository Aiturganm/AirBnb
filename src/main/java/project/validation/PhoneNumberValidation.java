package project.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import project.validation.validator.PasswordValidator;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
@Documented
@Constraint(validatedBy = {PasswordValidator.class})
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
public @interface PhoneNumberValidation {
    @Email
    String message() default "{Phone number start with '+996' and length 13 symbol}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
