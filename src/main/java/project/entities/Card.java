package project.entities;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "cards")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "card_gen",allocationSize = 1)
public class Card extends BaseEntity{
    private int carNumber;
    private BigDecimal money;
    @OneToOne(cascade = {CascadeType.PERSIST})
    private User user;
}
