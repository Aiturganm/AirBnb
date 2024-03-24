package project.dto.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
@AllArgsConstructor
public class CardRes {
    private int carNumber;
    private BigDecimal money;
}
