package project.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.dto.request.FeedBackRequest;
import project.dto.request.HouseRequest;
import project.dto.response.*;
import project.enums.HouseType;
import project.service.FeedBackService;

import java.security.Principal;

@RestController
@RequestMapping("/api/feedBack")
@RequiredArgsConstructor
public class FeedBackApi {
    private final FeedBackService feedBackService;


    @PermitAll
    @Operation(summary = "Сохранить отзыв о доме",
            description = "Метод для сохранения отзыва о доме и обновления рейтинга дома")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно сохранен отзыв и обновлен рейтинг дома"),
            @ApiResponse(responseCode = "404", description = "Дом с указанным ID не найден")
    })
    @PostMapping("/saveFeedBack/{houseId}")
    public SimpleResponse saveFeedback(@RequestBody @Valid FeedBackRequest feedBackRequest, Principal principal, @PathVariable
    Long houseId) {
        return feedBackService.saveFeedBack(feedBackRequest, principal, houseId);
    }

    @Operation(summary = "Получить отзыв по его идентификатору",
            description = "Метод для получения отзыва по его уникальному идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получен отзыв"),
            @ApiResponse(responseCode = "404", description = "Отзыв с указанным ID не найден")
    })
    @GetMapping("/getFeedBackById/{feedId}")
    public FeedBackResponse getFeedback(@PathVariable Long feedId) {
        return feedBackService.getFeedBack(feedId);
    }

    @PermitAll
    @Operation(summary = "Обновить отзыв",
            description = "Метод для обновления отзыва по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно обновлен отзыв"),
            @ApiResponse(responseCode = "403", description = "Отзыв принадлежит другому пользователю"),
            @ApiResponse(responseCode = "404", description = "Отзыв с указанным ID не найден")
    })
    @PutMapping("/updateFeed/{feedId}")
    public SimpleResponse updateFeed(@RequestBody @Valid  FeedBackRequest feedBackRequest, @PathVariable Long feedId, Principal principal) {
        return feedBackService.updateFeed(feedBackRequest, feedId, principal);
    }

    @Operation(summary = "Удалить отзыв",
            description = "Метод для удаления отзыва по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно удален отзыв"),
            @ApiResponse(responseCode = "403", description = "Отзыв принадлежит другому пользователю"),
            @ApiResponse(responseCode = "404", description = "Отзыв с указанным ID не найден")
    })
    @DeleteMapping("/deleteFeedBack/{feedId}")
    public SimpleResponse delete(@PathVariable Long feedId, Principal principal) {
        return feedBackService.delete(feedId, principal);
    }
    @Operation(summary = "Получить все отзывы по идентификатору дома",
            description = "Метод для получения всех отзывов по идентификатору дома с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получены отзывы"),
            @ApiResponse(responseCode = "404", description = "Дом с указанным ID не найден")
    })
    @GetMapping("/getAllFeedBackByHouseId/{houseId}")
    public PaginationFeedBack getAllFeedBeakInOneHouse(@PathVariable Long houseId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "3") int size) {
        return feedBackService.getAllFeedBack(houseId, page, size);
    }
    @Operation(summary = "Лайк или Удалит Лайк для обратной связи.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное добавление или удаление лайка для обратной связи."),
            @ApiResponse(responseCode = "404", description = "Ошибка: коммент  не найдена.")
    })
    @PostMapping("/likeFeedBack/{feedBackId}")
    public SimpleResponse like(@PathVariable Long feedBackId){
        return feedBackService.like(feedBackId);
    }
    @Operation(summary = "ДизЛайк или Удалит ДизЛайк для обратной связи.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешное добавление или удаление лайка для обратной связи."),
            @ApiResponse(responseCode = "404", description = "Ошибка: коммент  не найдена.")
    })
    @PostMapping("/disLikeFeedBack/{feedBackId}")
    public SimpleResponse dislike(@PathVariable Long feedBackId){
        return feedBackService.disLike(feedBackId);
    }
}
