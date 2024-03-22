package project.dto.request;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.enums.HouseType;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter


public class HouseRequest{

     String nameOfHotel;
     String description;
    @ElementCollection
    private List<String> images;
    private byte room;
    @Enumerated(EnumType.STRING)
    private HouseType houseType;
    private BigDecimal price;
    private int guests;

}
