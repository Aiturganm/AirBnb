package project.dto.response;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.enums.HouseType;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
@Builder
public class HouseResponsesClass {
    private Long id;
    private String nameOfHotel;
    private String description;
    private List<String> images;
    private byte room;
    private HouseType houseType;
    private BigDecimal price;
    private int guests;

    public HouseResponsesClass(Long id, String nameOfHotel, String description, List<String> images, byte room, HouseType houseType, BigDecimal price, int guests) {
        this.id = id;
        this.nameOfHotel = nameOfHotel;
        this.description = description;
        this.images = images;
        this.room = room;
        this.houseType = houseType;
        this.price = price;
        this.guests = guests;
    }
}
