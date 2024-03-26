package project.dto.response;
import lombok.*;
import project.enums.HouseType;
import java.math.BigDecimal;
import java.util.List;
@Setter
@Getter
@NoArgsConstructor

@Builder
public class HouseResForAdmin {
    private Long id;
    private String nameOfHotel;
    private String description;
    private Byte room;
    private HouseType houseType;
    private BigDecimal price;
    private Double rating;
    private Boolean isBooked;
    private Integer guests;
    private Boolean isPublished;
    private Boolean isBlock;
    private String reason;
    private Long feedbackCount;
    private Long favoriteCount;
    private Long rentInfoCount;

    public HouseResForAdmin(Long id, String nameOfHotel, String description, Byte room, HouseType houseType, BigDecimal price, Double rating, Boolean isBooked, Integer guests, Boolean isPublished, Boolean isBlock, String reason, Long feedbackCount, Long favoriteCount, Long rentInfoCount) {
        this.id = id;
        this.nameOfHotel = nameOfHotel;
        this.description = description;
        this.room = room;
        this.houseType = houseType;
        this.price = price;
        this.rating = rating;
        this.isBooked = isBooked;
        this.guests = guests;
        this.isPublished = isPublished;
        this.isBlock = isBlock;
        this.reason = reason;
        this.feedbackCount = feedbackCount;
        this.favoriteCount = favoriteCount;
        this.rentInfoCount = rentInfoCount;
    }
}
