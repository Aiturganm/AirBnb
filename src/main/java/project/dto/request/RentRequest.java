package project.dto.request;

import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;

public record RentRequest (
        Long houseId,
        LocalDate checkIn,
        LocalDate checkOut
){
}
