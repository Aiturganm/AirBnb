package project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "likes")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "like_gen",allocationSize = 1)
public class Like extends BaseEntity{
    @ManyToOne
    private User user;
}
