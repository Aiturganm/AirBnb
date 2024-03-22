package project.entities;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.LifecycleState;
import project.enums.HouseType;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "houses")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "house_gen",allocationSize = 1)
public class House extends BaseEntity{
    private String nameOfHotel;
    private String description;
    @ElementCollection
    private List<String> images;
    private byte room;
    @Enumerated(EnumType.STRING)
    private HouseType houseType;
    private BigDecimal price;
    private byte rating;
    private boolean isBooked;
    private int guests;
    private boolean isPublished;
    private boolean isBlock;

    @OneToOne
    private Address address;
}
