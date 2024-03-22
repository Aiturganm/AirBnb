package project.entities;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.LifecycleState;
import project.enums.HouseType;

import java.math.BigDecimal;
import java.util.ArrayList;
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

    @OneToMany(mappedBy = "house")
    private List<Feedback> feedbacks;
    @ManyToMany
    private List<Favorite> favorites;
    @ManyToOne
    private User user;
    @OneToMany(mappedBy = "house")
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
}
