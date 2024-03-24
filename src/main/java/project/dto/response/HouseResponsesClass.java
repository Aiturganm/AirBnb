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
@AllArgsConstructor
public class HouseResponsesClass {
    private Long id;
    private String nameOfHotel;
    private String description;
    private List<String> images;
    private byte room;
    private HouseType houseType;
    private BigDecimal price;
    private int guests;
}
