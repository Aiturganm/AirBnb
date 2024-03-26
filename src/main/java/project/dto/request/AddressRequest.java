package project.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import project.enums.Region;

@Builder
public record AddressRequest(
        @NotBlank
        String city,
        @NotBlank
        String street
) {
}
