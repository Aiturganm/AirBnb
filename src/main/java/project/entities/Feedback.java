package project.entities;

import jakarta.persistence.*;
import lombok.*;
import project.dto.response.FeedBackResponse;

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
    private double rating;
    @ElementCollection
    List<String> images;
    @ElementCollection
    private List<Long> likes;
    @ElementCollection
    private List<Long> dislikes;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private User user;
    @ManyToOne(cascade = {CascadeType.PERSIST})
    private House house;

    public Feedback(String comm, List<String> images, byte rating) {
    }

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

    public FeedBackResponse convert() {
        return new FeedBackResponse(this.comment,this.rating,this.images);
    }
}
