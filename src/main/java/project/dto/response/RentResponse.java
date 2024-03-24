package project.dto.response;

import org.springframework.http.HttpStatus;

import java.math.BigDecimal;

public record RentResponse (
        HttpStatus httpStatus,
        String message,
        BigDecimal totalPrice
){
}
