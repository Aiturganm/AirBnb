package project.dto.request;

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


public class HouseRequest{
         String description;
         byte room;
        @Enumerated(EnumType.STRING)
         HouseType houseType;
        @ElementCollection
         List<String> images;

}
