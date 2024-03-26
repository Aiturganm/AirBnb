package project.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record RentResponse (
        Long houseId,
        Long userId,
        String checkIn_checkOut,
        BigDecimal totalPrice
){
}
