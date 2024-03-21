package project.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
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
    private Region region;
    private String city;
    private String street;
}
