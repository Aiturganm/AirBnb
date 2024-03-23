package project.dto.response;

import lombok.Builder;
import project.entities.User;

import java.util.List;

@Builder
public record PaginationUserResponse(
        int page,
        int size,
        List<UserResponse> userResponses
) {
}
