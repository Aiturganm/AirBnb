package project.entities;
import jakarta.persistence.*;
import lombok.*;
import project.dto.response.CardRes;

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

    public void addMoney(BigDecimal newMoney){
        if(this.money == null) this.money = BigDecimal.valueOf(0);
        BigDecimal add = this.money.add(newMoney);
        setMoney(add);
    }

    public void subtractMoney(BigDecimal newMoney){
        BigDecimal subtract = this.money.subtract(newMoney);
        setMoney(subtract);
    }

    public Card( int carNumber, BigDecimal money) {
        this.carNumber = carNumber;
        this.money = money;
    }

    public CardRes convert() {
        return new CardRes(this.carNumber,this.money);
    }
}
