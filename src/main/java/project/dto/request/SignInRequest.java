package project.dto.request;

import lombok.Builder;
import project.validation.EmailValidation;
import project.validation.PasswordValidation;

@Builder
public record SignInRequest(@EmailValidation String email,@PasswordValidation String password) {
}
