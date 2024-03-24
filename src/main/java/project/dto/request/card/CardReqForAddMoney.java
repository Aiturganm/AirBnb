package project.dto.request.card;
import project.validation.PriceValidation;
import java.math.BigDecimal;
public record CardReqForAddMoney (@PriceValidation BigDecimal money){
}
