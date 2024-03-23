package project.dto.request;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import project.enums.HouseType;
import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
public class HouseRequest {
    String nameOfHotel;
    String description;
    private List<String> images;
    private byte room;
    private HouseType houseType;
    private BigDecimal price;
    private int guests;

}
