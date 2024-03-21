package project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.LockSupport;

@Entity
@Table(name = "announcements")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "annauncement_gen",allocationSize = 1)
public class Announcement extends BaseEntity{
    private BigDecimal price;
    private byte rating;
    private boolean isBooked;
    private byte guests;
    private boolean isPublished;
    //Relations
    @OneToMany
    private List<Feedback> feedbacks= new ArrayList<>();
    @OneToOne
    private House house;
    @OneToMany
    private List<Like> likes = new ArrayList<>();
}
