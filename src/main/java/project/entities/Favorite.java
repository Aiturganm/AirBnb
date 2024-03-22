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
    @OneToOne
    private User user;
    @ManyToMany
    private List<House> houses;
}
