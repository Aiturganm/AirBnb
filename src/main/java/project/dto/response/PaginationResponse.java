package project.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PaginationResponse(
        int page,
        int size,
        List<HouseResponse> houseResponseList
) {
}
