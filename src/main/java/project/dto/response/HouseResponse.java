package project.dto.response;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import project.enums.HouseType;

import java.util.List;
@Getter
@Setter
@Builder
public record HouseResponse (
    Long id,
    String description,
    byte room,
    @Enumerated(EnumType.STRING)
    HouseType houseType,
    @ElementCollection
    List<String> images
){
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
         int guests

                ){
}
