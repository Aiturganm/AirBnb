package project.service;

import project.api.RegisterResponse;
import project.dto.request.SignUpRequest;

public interface UserService {
    RegisterResponse signUp(SignUpRequest signUpRequest);
}
