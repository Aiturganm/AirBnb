package project.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import project.dto.request.SignInRequest;
import project.dto.request.SignUpRequest;
import project.dto.response.SignResponse;
import project.dto.response.SimpleResponse;
import project.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthApi {
    private final UserService userService;
    @Operation(summary = "Регистрация нового пользователя",
            description = "Метод для регистрации нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Электронная почта уже существует")
    })
    @PostMapping("/signUp")
    public SimpleResponse signUp(@RequestBody @Valid SignUpRequest signUpRequest){
        return userService.signUp(signUpRequest);
    }

    @Operation(summary = "Вход пользователя",
            description = "Метод для аутентификации пользователя и генерации JWT-токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный вход пользователя"),
            @ApiResponse(responseCode = "403", description = "Неверный пароль")
    })
    @PutMapping("/signIn")
    public SignResponse signIn(@RequestBody @Valid  SignInRequest signInRequest) {
        return userService.signIn(signInRequest);
    }

}
