package project.api;

import lombok.Builder;
import project.dto.response.SimpleResponse;

@Builder
public record RegisterResponse(
        String token,
        SimpleResponse simpleResponse
) {
}
