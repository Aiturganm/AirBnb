package project.api;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.dto.request.SignUpRequest;
import project.dto.response.RegisterResponse;
import project.dto.response.SimpleResponse;
import project.service.UserService;

@RestController
@RequestMapping("api/users")
@RequiredArgsConstructor
@Slf4j
public class UserApi {
    private final UserService userService;

}
