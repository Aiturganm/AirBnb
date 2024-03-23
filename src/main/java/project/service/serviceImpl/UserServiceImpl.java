package project.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import project.entities.User;
import project.dto.response.RegisterResponse;
import project.dto.request.SignUpRequest;
import project.repository.UserRepository;
import project.service.UserService;
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
//    private final ;
    @Override
    public RegisterResponse signUp(SignUpRequest signUpRequest) {
//        userRepository.existsByEmail()
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
//        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(signUpRequest.getRole());

//        boolean exists = userRepo.existsByEmail(signUpRequest.getEmail());
//        if (exists) throw new AlreadyExistsException("Email already exists!!!");
///
//        userRepo.save(user);
//        String newToken = jwtService.createToken(user);
//        return RegisterResponse.builder()
//                .token(newToken)
//                .simpleResponse(SimpleResponse.builder()
//                        .httpStatus(HttpStatus.OK)
//                        .message("Success user saved!!!")
//                        .build())
//                .build();
        return null;
    }
}
