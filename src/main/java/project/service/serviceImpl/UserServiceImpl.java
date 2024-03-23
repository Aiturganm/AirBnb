package project.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.api.RegisterResponse;
import project.config.jwt.JwtService;
import project.dto.request.SignInRequest;
import project.dto.request.SignUpRequest;
import project.dto.response.SignResponse;
import project.dto.response.SimpleResponse;
import project.entities.User;
import project.exception.AlreadyExistsException;
import project.exception.ForbiddenException;
import project.repository.UserRepository;
import project.service.UserService;
@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    @Override
    public RegisterResponse signUp(SignUpRequest signUpRequest) {
        boolean exist = userRepository.existsByEmail(signUpRequest.getEmail());
        if (exist) throw new AlreadyExistsException("Email already exists!!!");
//    private final ;
//        userRepository.existsByEmail()
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());

      user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(signUpRequest.getRole());
        userRepository.save(user);

        String token = jwtService.createToken(user);
        return RegisterResponse.builder()
                .token(token)
                .simpleResponse(SimpleResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("Success user saved!!!")
                        .build())
                .build();

    }

    @Override
    public SignResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.getByEmail(signInRequest.email());
        String password = user.getPassword();
        String decodePassword = signInRequest.password();
        boolean matches = passwordEncoder.matches(password, decodePassword);
        if (!matches){
            throw new ForbiddenException("Forbidden 403(wrong password)!");
        }
        String token = jwtService.createToken(user);

        return SignResponse.builder()
                .token(token)
                .id(user.getId())
                .email(user.getEmail())
                .role(user.getRole())
                .message("Success logIn!")
                .build();
      
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
