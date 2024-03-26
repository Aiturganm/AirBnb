package project.service;


import project.dto.request.SignInRequest;
import project.dto.request.SignUpRequest;
import project.dto.request.UserRequest;
import project.dto.response.*;
import project.entities.House;
import project.enums.Region;

import java.util.List;
import java.util.Map;

public interface UserService {
    SimpleResponse signUp(SignUpRequest signUpRequest);
    SignResponse signIn(SignInRequest signInRequest);

    PaginationUserResponse findAll(int page, int size);

    SimpleResponse update(SignUpRequest signUpRequest);

    SimpleResponse delete();

    UserResponse findById(Long userId);

    UserResForAdmin findByIdForAdmin(Long userId);

//    Map<Region, List<HouseResponsesClass>> filterByRegion(Region region);
}
