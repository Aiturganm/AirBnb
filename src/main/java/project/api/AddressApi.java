package project.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import project.dto.request.AddressRequest;
import project.dto.response.HouseResponse;
import project.dto.response.HouseResponsesClass;
import project.dto.response.SimpleResponse;
import project.enums.Region;
import project.service.AddressService;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressApi {
    //
    private final AddressService addressService;

    @Secured("VENDOR")
    @Operation(summary = "Обновить информацию о доме",
            description = "Метод для обновления Адрес о доме (адрес и регион)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о доме успешно обновлена"),
            @ApiResponse(responseCode = "404", description = "Дом не найден"),
            @ApiResponse(responseCode = "403", description = "Нет прав на обновление этого дома"),
            @ApiResponse(responseCode = "409", description = "Улица должна быть уникальной")
    })
    @PutMapping("/updateByHouseId/{houseId}")
    public SimpleResponse update(@PathVariable Long houseId,
                                 @RequestParam Region region,
                                 @RequestBody @Valid AddressRequest addressRequest) {
        return addressService.update(houseId, region, addressRequest);
    }
}
