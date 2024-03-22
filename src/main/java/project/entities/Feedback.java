package project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "feedbacks")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "feedback_gen",allocationSize = 1)
public class Feedback extends BaseEntity{
    private String comment;
    private byte rating;
    @ElementCollection
    List<String> images;
    @ElementCollection
    private List<Long> likes;
    @ElementCollection
    private List<Long> dislikes;
    @ManyToOne
    private User user;

    public void addLikes(Long id){
        if(this.likes == null)  this.likes = new ArrayList<>();
        this.likes.add(id);
    }

    public void addDislikes(Long id) {
        if (this.dislikes == null){
            this.dislikes = new ArrayList<>();
        }
        this.dislikes.add(id);
    }
}
