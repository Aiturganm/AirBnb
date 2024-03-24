package project.api;

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
    @PostMapping("/save")
    public SimpleResponse saveCard(@RequestBody @Valid CardReq cardReq){
        return cardService.save(cardReq);
    }
    @GetMapping("/getMyCard")
    public CardRes getMyCard(){
        return cardService.getMyCard();
    }
    @PutMapping("/addMoney")
    public SimpleResponse updateMyCard(@RequestBody @Valid CardReqForAddMoney money){
        return cardService.update(money.money());
    }
    @DeleteMapping("/deleteMyCard")
    public SimpleResponse delete(){
        return cardService.delete();
    }
}
