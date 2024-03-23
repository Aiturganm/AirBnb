package project.service;

import project.api.RegisterResponse;
import project.dto.request.SignUpRequest;
import project.dto.response.RegisterResponse;

public interface UserService {
    //    private final ;
    RegisterResponse signUp(SignUpRequest signUpRequest);
}
