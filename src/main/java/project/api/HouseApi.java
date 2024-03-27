package project.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.dto.request.HouseRequest;
import project.dto.response.*;
import project.enums.HouseType;
import project.enums.Region;
import project.service.HouseService;

import java.math.BigDecimal;
import java.security.Principal;

@RestController
@RequestMapping("/api/house")
@RequiredArgsConstructor
public class HouseApi {
    private final HouseService houseService;

    @PermitAll
    @Operation(summary = "Сохранить новый дом",
            description = "Метод для сохранения нового дома в системе")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Дом успешно сохранен"),
            @ApiResponse(responseCode = "400", description = "Ошибка в запросе или пользователь заблокирован"),
            @ApiResponse(responseCode = "401", description = "Неавторизованный запрос"),
            @ApiResponse(responseCode = "404", description = "Регион или улица не найдены")
    })
    @PostMapping("/saveHouse")
    public SimpleResponse saveHouse(@RequestBody @Valid HouseRequest houseRequest, Principal principal, @RequestParam HouseType houseType,@RequestParam Region region) {
        return houseService.saveHouse(houseRequest , principal, houseType,region);
    }

    @PermitAll
    @Operation(summary = "Получить информацию о доме по идентификатору",
            description = "Метод для получения информации о доме по его идентификатору")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о доме успешно получена"),
            @ApiResponse(responseCode = "404", description = "Дом не найден по указанному идентификатору")
    })
    @GetMapping("/getHouseById/{houseId}")
    public HouseFeedBackResponse findById(@PathVariable Long houseId) {
        return houseService.findbyId(houseId);
    }

    @PermitAll
    @Operation(summary = "Получить опубликованные дома",
            description = "Метод для получения списка всех опубликованных домов с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список опубликованных домов успешно получен")
    })
    @GetMapping("/findAllHousesPublishedHouses")
    public PaginationResponse findAllPublishedHouses(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size){
        return houseService.findAllPublished(page,size);
    }
    @Secured("ADMIN")
    @Operation(summary = "Получить все дома",
            description = "Метод для получения списка всех домов с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список всех домов успешно получен")
    })
    @GetMapping("/findAllHouses")
    public PaginationResponse allHouses(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return houseService.allHouses(page,size);
    }

    @Secured("ADMIN")
    @Operation(summary = "Получить непубликованные дома",
            description = "Метод для получения списка всех непубликованных домов с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список всех непубликованных домов успешно получен")
    })
    @GetMapping("/findAllNotPublishedHouses")
    public PaginationResponse notAllHouses(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return houseService.notPublishedHouses(page,size);    }

    @Secured({"VENDOR","ADMIN"})
    @Operation(summary = "Обновить информацию о доме",
            description = "Метод для обновления информации о доме пользователем или администратором")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Информация о доме успешно обновлена"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для обновления информации о доме"),
            @ApiResponse(responseCode = "404", description = "Дом с указанным идентификатором не найден")
    })
    @PutMapping("/updateHouse/{houseId}")
    public SimpleResponse updateHouse(@RequestBody  @Valid HouseRequest houseRequest, @PathVariable Long houseId, Principal principal, @RequestParam HouseType houseType){
        return houseService.updateHouse(houseRequest, houseId, principal,houseType);
    }

    @Secured("VENDOR")
    @Operation(summary = "Удалить дом",
            description = "Метод для удаления дома с арендами пользователем")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Дом успешно удален"),
            @ApiResponse(responseCode = "403", description = "Недостаточно прав для удаления дома"),
            @ApiResponse(responseCode = "404", description = "Дом с указанным идентификатором не найден")
    })
    @DeleteMapping("/deleteMyHouse/{houseId}")
    public SimpleResponse deleteHouse(@PathVariable Long houseId, Principal principal) {
        return houseService.deleteHouse(houseId, principal);
    }

    @PermitAll
    @Operation(summary = "Поиск домов по имени",
            description = "Метод для поиска домов по их названию")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список домов найден успешно"),
            @ApiResponse(responseCode = "404", description = "Дома с указанным именем не найдены")
    })
    @GetMapping("/getHouseByName")
    public PaginationResponse findByName(@RequestParam String houseName, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size) {
        return houseService.findByName(houseName, page, size);
    }

    @PermitAll
    @Operation(summary = "Поиск домов по идентификатору пользователя",
            description = "Метод для поиска домов по идентификатору пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список домов пользователя найден успешно"),
            @ApiResponse(responseCode = "404", description = "Пользователь с указанным идентификатором не найден")
    })
    @GetMapping("/getHouseByUserId/{userId}")
    public PagiUserHouse findByUserId(@PathVariable Long userId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size) {
        return houseService.findByUserId(userId, page, size); // Передаем userId в сервис
    }

    @PermitAll
    @Operation(summary = "Сортировать дома по цене",
            description = "Метод для получения списка домов с сортировкой по цене (по возрастанию или убыванию)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно отсортирован список домов"),
            @ApiResponse(responseCode = "404", description = "Неправильное значение сортировки")
    })
    @GetMapping("/sortByPrice")
    public PaginationResponse sortByPrice(@RequestParam String ascOrDesc,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return houseService.sortByPrice(ascOrDesc,page,size);
    }

    @PermitAll
    @ApiOperation("Возвращает пагинированный список домов с ценами в указанном диапазоне.")
    @GetMapping("/sortByBetweenPrice/{startPrice}/{FinishPrice}")
    public PaginationResponse betweenPrice(@PathVariable BigDecimal startPrice, @PathVariable BigDecimal FinishPrice,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return houseService.betweenPrice(startPrice, FinishPrice,page,size);
    }

    @PermitAll
    @Operation(summary = "Найти дома по региону (ТОЛЬКА ДЛЯ ПУБЛИЧНЫХ)",
            description = "Метод для поиска домов по указанному региону с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно найдены дома по указанному региону"),
            @ApiResponse(responseCode = "404", description = "Регион не найден")
    })
    @GetMapping("/getHouseByRegion")
    public PaginationResponse findByRegion(@RequestParam Region region,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return houseService.findByRegion(region,page,size);
    }

    @PermitAll
    @Operation(summary = "Фильтровать дома по типу (ТОЛЬКА ДЛЯ ПУБЛИЧНЫХ)",
            description = "Метод для фильтрации домов по указанному типу с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно отфильтрованы дома по указанному типу")
    })
    @GetMapping("/filterByHomeType")
    public PaginationResponse filterByType(@RequestParam HouseType type,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return houseService.filterByType(type,page,size);
    }
    @PermitAll
    @Operation(summary = "Популярные дома(ТОЛЬКА ДЛЯ ПУБЛИЯНЫХ)",
            description = "Метод для получения списка популярных домов с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получен список популярных домов")
    })
    @GetMapping("/findPopular")
    public PaginationResponse popularsHouses(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return houseService.popularHouses(page,size);
    }

}


