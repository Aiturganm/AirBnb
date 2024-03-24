package project.dto.response;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import project.enums.HouseType;

import java.math.BigDecimal;
import java.util.List;


@Builder
public record HouseResponse (
        Long id,
        String nameOfHotel,
        String description,
        @ElementCollection
         List<String> images,
         byte room,
        @Enumerated(EnumType.STRING)
         HouseType houseType,
         BigDecimal price,
         byte rating,
         int guests

                ){
}
