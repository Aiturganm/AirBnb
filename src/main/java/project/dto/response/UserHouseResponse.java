package project.dto.response;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.enums.HouseType;

import java.math.BigDecimal;
import java.util.List;

@Builder
public record UserHouseResponse(
        String userName,
        Long id,
        String nameOfHotel,
        String description,
        @ElementCollection
        List<String> images,
        byte room,
        @Enumerated(EnumType.STRING)
        HouseType houseType,
        BigDecimal price,
        int guests

) {
}
