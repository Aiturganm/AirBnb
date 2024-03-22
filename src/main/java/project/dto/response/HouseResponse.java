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
}
