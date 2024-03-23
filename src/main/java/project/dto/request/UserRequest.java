package project.dto.request;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import project.enums.Role;

import java.time.LocalDate;

@Builder
public record UserRequest(
        String firstName,
        String lastName,
        String email,
        String password,
        LocalDate dateOfBirth,
        Role role,
        boolean isBlock,
        String phoneNumber
) {
}
