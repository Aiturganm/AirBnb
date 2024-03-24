package project.dto.response;

import lombok.Builder;

import java.util.List;

@Builder
public record PagiUserHouse(
        int page,
        int size,
        List<UserHouseResponse> houseResponseList
) {
}
