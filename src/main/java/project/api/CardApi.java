package project.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.dto.request.card.CardReq;
import project.dto.request.card.CardReqForAddMoney;
import project.dto.response.CardRes;
import project.dto.response.SimpleResponse;
import project.service.CardService;
@RequiredArgsConstructor
@RestController
@RequestMapping("api/card")
public class CardApi {
    private final CardService cardService;

    @Operation(summary = "Сохранить кредитную карту",
            description = "Метод для сохранения информации о кредитной карте пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно сохранена информация о кредитной карте"),
            @ApiResponse(responseCode = "400", description = "Карта с таким номером уже существует"),
            @ApiResponse(responseCode = "409", description = "У пользователя уже есть карта")
    })
    @PostMapping("/save")
    public SimpleResponse saveCard(@RequestBody @Valid CardReq cardReq){
        return cardService.save(cardReq);
    }
    @Operation(summary = "Получить информацию о карте пользователя",
            description = "Метод для получения информации о кредитной карте текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получена информация о карте пользователя"),
            @ApiResponse(responseCode = "400", description = "У пользователя нет карты")
    })
    @GetMapping("/getMyCard")
    public CardRes getMyCard(){
        return cardService.getMyCard();
    }
    @Operation(summary = "Пополнить счет",
            description = "Метод для пополнения баланса карты текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно пополнен баланс карты"),
    })
    @PutMapping("/addMoney")
    public SimpleResponse updateMyCard(@RequestBody @Valid CardReqForAddMoney money){
        return cardService.update(money.money());
    }
    @Operation(summary = "Удалить карту",
            description = "Метод для удаления информации о кредитной карте текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно удалена информация о карте пользователя")
    })
    @DeleteMapping("/deleteMyCard")
    public SimpleResponse delete(){
        return cardService.delete();
    }
}
