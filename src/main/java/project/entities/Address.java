package project.entities;

import jakarta.persistence.*;
import lombok.*;
import project.enums.Region;

@Entity
@Table(name = "addresses")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "address_gen",allocationSize = 1)
public class Address extends BaseEntity{
    @Enumerated(EnumType.STRING)
    private Region region;
    private String city;
    private String street;
    @OneToOne(cascade = {CascadeType.REMOVE})
    private House house;
}
