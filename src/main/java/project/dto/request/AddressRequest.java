package project.dto.request;

import lombok.Builder;
import project.enums.Region;

@Builder
public record AddressRequest(
        String city,
        String street
) {
}
