package project.dto.response;

import lombok.Builder;
import project.enums.HouseType;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record HouseFeedBackResponse(
        Long id,
        String nameOfHotel,
        String description,
        List<String> images,
        byte room,
        HouseType houseType,
        BigDecimal price,
        double rating,
        int guests,
        List<FeedBackResponse>feedBackResponses
) {
}
