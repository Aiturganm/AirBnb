package project.dto.response;

import lombok.Builder;
import project.enums.Role;

@Builder
public record SignResponse(
        String token,
        Long id,
        Role role,
        String email,
        String message
) {
}
