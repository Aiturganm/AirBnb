package project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "rentInfos")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "rent_gen",allocationSize = 1)
public class RentInfo extends BaseEntity{
    private LocalDate checkIn;
    private LocalDate checkOut;
    private BigDecimal totalPrice;

    @ManyToOne
    private User user;
    @ManyToOne
    private House house;
}
