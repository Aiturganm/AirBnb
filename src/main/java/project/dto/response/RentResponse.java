package project.dto.response;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record RentResponse (
        Long houseId,
        Long userId,
        String checkIn_checkOut,
        BigDecimal totalPrice
){
}
