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
        List<String> images,
        byte room,
        HouseType houseType,
        BigDecimal price,
        double rating,
        int guests
) {}

