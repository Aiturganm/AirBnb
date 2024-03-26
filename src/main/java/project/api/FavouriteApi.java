package project.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.web.bind.annotation.*;
import project.dto.response.PaginationResponse;
import project.dto.response.SimpleResponse;
import project.service.FavoriteService;

@RestController
@RequestMapping("/api/favourite")
@RequiredArgsConstructor
public class FavouriteApi {
    private final FavoriteService favoriteService;
    @Operation(summary = "Добавить или удалить избранное",
            description = "Метод для добавления или удаления дома из списка избранных текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно добавлен или удален дом из списка избранных"),
            @ApiResponse(responseCode = "404", description = "Дом с указанным ID не найден"),
            @ApiResponse(responseCode = "404", description = "Пользователь с указанным email не найден")
    })
    @PostMapping("/save/{houseId}")
    public SimpleResponse likeOrUnLike(@PathVariable Long houseId){
        return favoriteService.save(houseId);
    } @Operation(summary = "Получить избранные дома текущего пользователя",
            description = "Метод для получения списка избранных домов текущего пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получен список избранных домов")
    })
    @GetMapping("/getMyFavoriteHouses")
    public PaginationResponse getMyFav(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return favoriteService.getMy(page,size);
    }

}
