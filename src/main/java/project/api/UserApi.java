package project.api;

import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.dto.request.SignUpRequest;
import project.dto.response.*;
import project.service.UserService;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Slf4j
public class UserApi {
    private final UserService userService;

    //
    @Operation(summary = "Возвращает пагинированный список всех пользователей.")
    @GetMapping("/findAll")
    public PaginationUserResponse findAll(@RequestParam int page,
                                          @RequestParam int size) {
        return userService.findAll(page, size);
    }

    @PutMapping("/update")
    @Operation(summary = "Обновляет информацию о пользователе.")
    public SimpleResponse update(
            @RequestBody SignUpRequest signUpRequest) {
        return userService.update(signUpRequest);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Удаляет текущего пользователя и все его дома.")
    public SimpleResponse delete() {
        return userService.delete();
    }


}
