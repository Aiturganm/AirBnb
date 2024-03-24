package project.dto.request;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import project.enums.HouseType;
import project.validation.MinesValidation;
import project.validation.PriceValidation;
import project.validation.RoomValidation;

import java.math.BigDecimal;
import java.util.List;
@Getter
@Setter
public class HouseRequest {
    @NotBlank
    String nameOfHotel;
    @NotBlank
    String description;
    @NotNull
    private List<String> images;
    @RoomValidation
    private byte room;
    @NotNull
    private HouseType houseType;
    @PriceValidation
    private BigDecimal price;
    @MinesValidation
    private int guests;

}
