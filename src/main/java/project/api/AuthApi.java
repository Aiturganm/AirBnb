package project.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import project.dto.request.SignInRequest;
import project.dto.request.SignUpRequest;
import project.dto.request.UserRequest;
import project.dto.response.SignResponse;
import project.dto.response.RegisterResponse;
import project.dto.response.SimpleResponse;
import project.service.UserService;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthApi {
    private final UserService userService;

    @Secured("ADMIN")
    @PostMapping("/save")
    public RegisterResponse signUp(@RequestBody SignUpRequest signUpRequest){
        log.info("success saved!!!");
        return userService.signUp(signUpRequest);
    }
    @GetMapping("signIn")
    public SignResponse signIn(@RequestBody SignInRequest signInRequest){
        return userService.signIn(signInRequest);
    }

}
