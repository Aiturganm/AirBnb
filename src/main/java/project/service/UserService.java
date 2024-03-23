package project.service;


import project.dto.request.SignInRequest;
import project.dto.request.SignUpRequest;
import project.dto.request.UserRequest;
import project.dto.response.PaginationUserResponse;
import project.dto.response.RegisterResponse;
import project.dto.response.SignResponse;
import project.dto.response.SimpleResponse;

public interface UserService {
    RegisterResponse signUp(SignUpRequest signUpRequest);

    SignResponse signIn(SignInRequest signInRequest);

    PaginationUserResponse findAll(int page, int size);

    SimpleResponse update(Long userId, UserRequest userRequest);
}
