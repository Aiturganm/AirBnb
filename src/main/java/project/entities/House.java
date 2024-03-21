package project.entities;

import jakarta.persistence.*;
import lombok.*;
import org.apache.catalina.LifecycleState;
import project.enums.HouseType;

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
    private String description;
    private byte room;
    @Enumerated(EnumType.STRING)
    private HouseType houseType;
    @ElementCollection
    private List<String> images;
    //Relations
    @OneToOne
    private Address address;
}
