package project.dto.request.card;

import jakarta.validation.constraints.NegativeOrZero;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.dto.response.SimpleResponse;
import project.entities.Card;
import project.validation.PriceValidation;

import java.math.BigDecimal;

@Builder
@Setter
@Getter
public class CardReq {
    @NotNull
    private int cardNumber;
    @PriceValidation
    private BigDecimal money;


    public Card convert() {
        return new Card(this.cardNumber,this.money);
    }
}
