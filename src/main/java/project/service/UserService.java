package project.service;

import project.api.RegisterResponse;
import project.dto.request.SignInRequest;
import project.dto.request.SignUpRequest;
import project.dto.response.SignResponse;

public interface UserService {
    RegisterResponse signUp(SignUpRequest signUpRequest);
    SignResponse signIn(SignInRequest signInRequest);
}
