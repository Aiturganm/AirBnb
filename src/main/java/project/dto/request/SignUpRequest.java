package project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.enums.Role;
import project.validation.DateOfBirthValidation;
import project.validation.EmailValidation;
import project.validation.PasswordValidation;
import project.validation.PhoneNumberValidation;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class SignUpRequest{
    @NotBlank
    private String firstName;
    @NotBlank
    private String lastName;
    @DateOfBirthValidation
    private LocalDate dateOfBirth;
    @EmailValidation
    private String email;
    @PasswordValidation
    private String password;
    @PhoneNumberValidation
    private String phoneNumber;
}
