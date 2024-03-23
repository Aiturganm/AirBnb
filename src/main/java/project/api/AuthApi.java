package project.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dto.request.SignUpRequest;
//import project.dto.response.RegisterResponse;
import project.service.UserService;

@RestController
@RequestMapping("api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthApi {
    private final UserService userService;

    @PostMapping("/save")
    public RegisterResponse signUp(@RequestBody SignUpRequest signUpRequest){
        log.info("success saved!!!");
        return userService.signUp(signUpRequest);
    }
//    @GetMapping
//    public SignResponse signIn(@RequestBody SignInRequest signInRequest){
//        return userService.signIn(signInRequest);
//    }
}
