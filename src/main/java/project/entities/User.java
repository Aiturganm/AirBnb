package project.entities;

import jakarta.persistence.*;
import lombok.*;
import project.enums.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "user_gen",allocationSize = 1)
public class User extends BaseEntity{
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean isBlock;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<House> houses;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<RentInfo> rentInfos;
    @OneToMany(mappedBy = "user", cascade = {CascadeType.REMOVE}, fetch = FetchType.EAGER)
    private List<Feedback> feedbacks;

    public void addHouse(House house){
        if(this.houses == null){
            this.houses = new ArrayList<>();
        }
        this.houses.add(house);
    }

    public void addRentInfo(RentInfo rentInfo){
        if(this.rentInfos == null){
            this.rentInfos = new ArrayList<>();
        }
        this.rentInfos.add(rentInfo);
    }

    public void addFeedback(Feedback feedback){
        if(this.feedbacks ==  null){
            this.feedbacks = new ArrayList<>();
        }
        this.feedbacks.add(feedback);
    }
}
