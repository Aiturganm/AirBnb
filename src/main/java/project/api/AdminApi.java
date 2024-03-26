package project.api;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import project.dto.request.AcceptOrRejectReq;
import project.dto.response.*;
import project.enums.ActionForHouse;
import project.enums.BlockOrUnBlock;
import project.enums.HouseType;
import project.enums.Region;
import project.service.HouseService;
import project.service.UserService;
import project.service.serviceImpl.AdminService;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/api")
public class AdminApi {
    private final UserService userService;
    private final HouseService houseService;
    private final AdminService adminService;

    @Secured("ADMIN")
    @Operation(summary = "Получить все опубликованные дома",
            description = "Получает список всех (опубликованных) домов с пагинацией. " +
                    "Для доступа к этому методу требуются права администратора.")
    @GetMapping("/getAllPublicHouse")
    public PaginationResponse allPublicHouses(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size) {
        return houseService.findAllPublished(page, size);
    }


    @Secured("ADMIN")
    @Operation(summary = "Получить все  дома",
            description = "Получает список всех  домов с пагинацией. " +
                    "Для доступа к этому методу требуются права администратора.")
    @GetMapping("/getAllHouses")
    public PaginationResponse allHouse(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size) {
        return houseService.allHouses(page, size);
    }

    @Secured("ADMIN")
    @PutMapping("/actionForHouse/{houseId}")
    @Operation(summary = "Принять или отклонить дом",
            description = "Метод для принятия или отклонения дома с указанным идентификатором")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно принят или отклонен дом или удален!"),
            @ApiResponse(responseCode = "404", description = "Дом не найден")
    })
    public SimpleResponse acceptOrReject(@PathVariable Long houseId, @RequestParam ActionForHouse actionForHouse, @RequestBody AcceptOrRejectReq acceptOrRejectReq, Principal principal) {
        return adminService.acceptOrReject(houseId, actionForHouse, principal, acceptOrRejectReq);
    }

    @Secured("ADMIN")
    @Operation(summary = "Блокировать или разблокировать дом",
            description = "Метод для блокировки или разблокировки дома с указанным идентификатором")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно выполнена блокировка или разблокировка дома"),
            @ApiResponse(responseCode = "404", description = "Дом не найден")
    })
    @PutMapping("/blockUnBlock/{houseId}")
    public SimpleResponse blockUnBlock(@PathVariable Long houseId, @RequestParam BlockOrUnBlock blockOrUnBlock) {
        return adminService.blockUnBlock(houseId, blockOrUnBlock);
    }

    @Secured("ADMIN")
    @Operation(summary = "Блокировать или разблокировать пользователя и его дома",
            description = "Метод для блокировки или разблокировки пользователя и всех его домов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно выполнена блокировка или разблокировка пользователя и его домов"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @PutMapping("/vendorBlockOrUnblock/{userId}")
    public SimpleResponse userBlockOrUnBlock(@PathVariable Long userId, @RequestParam BlockOrUnBlock blockOrUnBlock) {
        return adminService.userBlockOrUnBlock(userId, blockOrUnBlock);
    }

    @Secured("ADMIN")
    @Operation(summary = "Получить всех пользователей",
            description = "Метод для получения списка всех пользователей с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получен список пользователей")
    })
    @GetMapping("/getAllUsers")
    public PaginationUserResponse getAllUsers(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size) {
        return userService.findAll(page, size);
    }

    @Secured("ADMIN")
    @Operation(summary = "Сортировать дома по цене",
            description = "Метод для получения списка домов с сортировкой по цене (по возрастанию или убыванию)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно отсортирован список домов"),
            @ApiResponse(responseCode = "404", description = "Неправильное значение сортировки")
    })
    @GetMapping("/filterByPrice")
    public PaginationResponse sortByPrice(@RequestParam String ascOrDesc, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size) {
        return houseService.sortByPrice(ascOrDesc, page, size);
    }

    @Secured("ADMIN")
    @Operation(summary = "Найти дома по региону (ТОЛЬКА ДЛЯ ПУБЛИЧНЫХ)",
            description = "Метод для поиска домов по указанному региону с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно найдены дома по указанному региону"),
            @ApiResponse(responseCode = "404", description = "Регион не найден")
    })
    @GetMapping("/filterByRegion")
    public PaginationResponse findByRegion(@RequestParam Region region, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size) {
        return houseService.findByRegion(region, page, size);
    }

    @Secured("ADMIN")
    @Operation(summary = "Фильтровать дома по типу (ТОЛЬКА ДЛЯ ПУБЛИЧНЫХ)",
            description = "Метод для фильтрации домов по указанному типу с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно отфильтрованы дома по указанному типу")
    })
    @GetMapping("/filterByHomeType")
    public PaginationResponse filterByType(@RequestParam HouseType type, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size) {
        return houseService.filterByType(type, page, size);
    }

    @Secured("ADMIN")
    @Operation(summary = "Популярные дома(ТОЛЬКА ДЛЯ ПУБЛИЯНЫХ)",
            description = "Метод для получения списка популярных домов с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получен список популярных домов")
    })
    @GetMapping("/findPopular")
    public PaginationResponse popularsHouses(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size) {
        return houseService.popularHouses(page, size);
    }

    @Secured("ADMIN")
    @Operation(summary = "Получить пользователя для администратора",
            description = "Метод для получения информации о пользователе для администратора включая список его домов")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешно получена информация о пользователе"),
            @ApiResponse(responseCode = "404", description = "Пользователь не найден")
    })
    @GetMapping("/findById/{userId}")
    public UserResForAdmin findById(@PathVariable Long userId) {
        return userService.findByIdForAdmin(userId);
    }

    @Secured("ADMIN")
    @Operation(summary = "Получить непубликованные дома",
            description = "Метод для получения списка всех непубликованных домов с пагинацией")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Список всех непубликованных домов успешно получен")
    })
    @GetMapping("/findAllNotPublishedHouses")
    public PaginationResponse NotAllHouses(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size) {
        return houseService.notPublishedHouses(page, size);
    }
}
