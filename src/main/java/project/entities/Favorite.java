package project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "favorites")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "favorite_gen",allocationSize = 1)
public class Favorite extends BaseEntity{
    private LocalDate createdAt;
    @OneToOne(cascade = {CascadeType.PERSIST})
    private User user;
    @ManyToMany(cascade = {CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private List<House> houses;
}
