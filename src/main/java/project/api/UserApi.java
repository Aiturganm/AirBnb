package project.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.dto.response.SimpleResponse;
import project.service.UserService;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
public class UserApi {
    private final UserService userService;

    @PostMapping("/{restaurantsId}")
    public RegisterResponse signUp(@RequestBody SignUpRequest signUpRequest,
                                   @PathVariable Long restaurantsId){
        log.info("success saved!!!");
        return userService.signUp(restaurantsId,signUpRequest);
    }
    @GetMapping
    public SignResponse signIn(@RequestBody SignInRequest signInRequest){
        return userService.signIn(signInRequest);
    }

}
