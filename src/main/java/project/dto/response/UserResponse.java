package project.dto.response;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import project.enums.Role;

import java.time.LocalDate;
//@NoArgsConstructor
//@AllArgsConstructor
@Builder
public record UserResponse(
            String firstName,
            String lastName,
            String email,
            LocalDate dateOfBirth,
            Role role,
            boolean isBlock,
            String phoneNumber
) {
}
