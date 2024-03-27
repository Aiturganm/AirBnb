package project.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import project.enums.Role;
import project.validation.DateOfBirthValidation;
import project.validation.EmailValidation;
import project.validation.PasswordValidation;
import project.validation.PhoneNumberValidation;

import java.time.LocalDate;

@Builder
public record UserRequest(
        @NotBlank
        String firstName,
        @NotBlank
        String lastName,
        @EmailValidation
        String email,
        @PasswordValidation
        String password,
        @DateOfBirthValidation
        LocalDate dateOfBirth,
        Role role,
        boolean isBlock,
        @PhoneNumberValidation
        String phoneNumber
) {
}
