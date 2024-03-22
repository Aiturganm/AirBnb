package project.dto.response;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.enums.HouseType;

import java.util.List;
@Getter
@Setter
@Builder
public record UserHouseResponse(
        String userName,
        Long id,
        String description,
        byte room,
        @Enumerated(EnumType.STRING)
        HouseType houseType,
        @ElementCollection
        List<String> images

) {
}
