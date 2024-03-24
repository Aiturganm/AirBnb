package project.service;


import project.dto.request.SignInRequest;
import project.dto.request.SignUpRequest;
import project.dto.response.PaginationUserResponse;
import project.dto.response.RegisterResponse;
import project.dto.response.SignResponse;

public interface UserService {
    RegisterResponse signUp(SignUpRequest signUpRequest);
    SignResponse signIn(SignInRequest signInRequest);

    PaginationUserResponse findAll(int page, int size);
}
