package project.entities;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.LifecycleState;
import project.dto.response.HouseResponse;
import project.enums.HouseType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "houses")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "house_gen", sequenceName = "house_seq",allocationSize = 1, initialValue = 1)
public class House extends BaseEntity{
    private String nameOfHotel;
    private String description;
    @ElementCollection
    private List<String> images;
    private byte room;
    @Enumerated(EnumType.STRING)
    private HouseType houseType;
    private BigDecimal price;
    private double rating;
    private boolean isBooked;
    private int guests;
    private boolean isPublished;
    private boolean isBlock;
    private String reason;

    public House( String nameOfHotel, String description, List<String> images, byte room, HouseType houseType, BigDecimal price, byte rating, boolean isBooked, int guests, boolean isPublished, boolean isBlock) {
        this.nameOfHotel = nameOfHotel;
        this.description = description;
        this.images = images;
        this.room = room;
        this.houseType = houseType;
        this.price = price;
        this.rating = rating;
        this.isBooked = isBooked;
        this.guests = guests;
        this.isPublished = isPublished;
        this.isBlock = isBlock;
    }

    @OneToMany(mappedBy = "house", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<Feedback> feedbacks;
    @ManyToMany(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Favorite> favorites;
    @ManyToOne(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private User user;
    @OneToMany(mappedBy = "house", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<RentInfo> rentInfos;

    public void addFeedback(Feedback feedback){
        if(this.feedbacks == null){
            this.feedbacks = new ArrayList<>();
        }
        this.feedbacks.add(feedback);
    }

    public void addFavorite(Favorite favorite){
        if(this.favorites == null){
            this.favorites = new ArrayList<>();
        }
        this.favorites.add(favorite);
    }

    public void addRentInfo(RentInfo rentInfo){
        if(this.rentInfos == null){
            this.rentInfos = new ArrayList<>();
        }
        this.rentInfos.add(rentInfo);
    }

    public HouseResponse convert() {
        return new HouseResponse(super.getId(),this.nameOfHotel,this.description,this.images,this.room,this.houseType,this.price,this.rating,this.guests);
    }
}
