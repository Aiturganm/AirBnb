package project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "feedback")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "feed_gen",allocationSize = 1)
public class Feedback extends BaseEntity{
    private String comment;
    private byte rating;
    private String image;
    //Relations
    @OneToMany
    private List<Like> likes = new ArrayList<>();
    @ManyToOne
    private User user;
}
