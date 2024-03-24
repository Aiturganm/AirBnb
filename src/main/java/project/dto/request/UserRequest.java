package project.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import lombok.Builder;
import project.enums.Role;
import project.validation.EmailValidation;

import java.time.LocalDate;

@Builder
public record UserRequest(
        String firstName,
        String lastName,
        @EmailValidation
        String email,
        String password,
        LocalDate dateOfBirth,
        Role role,
        boolean isBlock,
        String phoneNumber
) {
}
