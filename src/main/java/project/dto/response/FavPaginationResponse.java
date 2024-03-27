package project.dto.response;

import lombok.Builder;
import project.entities.Favorite;
import project.entities.House;

import java.util.List;
import java.util.Map;

@Builder
public record FavPaginationResponse(
        int page,
        int size,
        Map<Favorite, House> favorites
        ) {

}
