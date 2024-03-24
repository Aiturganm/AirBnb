package project.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.config.jwt.JwtService;
import project.dto.request.SignInRequest;
import project.dto.request.SignUpRequest;
import project.dto.request.UserRequest;
import project.dto.response.PaginationUserResponse;
import project.dto.response.RegisterResponse;
import project.dto.response.SignResponse;
import project.dto.response.SimpleResponse;
import project.dto.response.UserResponse;
import project.entities.House;
import project.entities.User;
import project.exception.AlreadyExistsException;
import project.exception.BadRequestException;
import project.exception.ForbiddenException;
import project.exception.NotFoundException;
import project.repository.UserRepository;
import project.service.UserService;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public SimpleResponse signUp(SignUpRequest signUpRequest) {
        boolean exist = userRepository.existsByEmail(signUpRequest.getEmail());
        if (exist) throw new AlreadyExistsException("Email already exists!!!");
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());

        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setRole(signUpRequest.getRole());
        userRepository.save(user);
        return SimpleResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("Success user saved!!!")
                        .build();
    }

    @Override
    public SignResponse signIn(SignInRequest signInRequest) {
        User user = userRepository.getByEmail(signInRequest.email());
        String password = user.getPassword();
        String decodePassword = signInRequest.password();
        boolean matches = passwordEncoder.matches(decodePassword,password);
        if (!matches) {
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
    }

    @Override
    public PaginationUserResponse findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<User> usersPage = userRepository.findAll(pageable);
        List<UserResponse> userResponses = new ArrayList<>();
        for (User user : usersPage) {
            UserResponse userResponse = UserResponse.builder()
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .dateOfBirth(user.getDateOfBirth())
                    .role(user.getRole())
                    .isBlock(user.isBlock())
                    .phoneNumber(user.getPhoneNumber())
                    .build();

            userResponses.add(userResponse);
        }
        return PaginationUserResponse.builder()
                .page(usersPage.getNumber() + 1)
                .size(size)
                .userResponses(userResponses)
                .build();
    }

    @Override
    public SimpleResponse update(Long userId, UserRequest userRequest) {
        for (User user1 : userRepository.findAll()) {
            if (!user1.getEmail().equals(userRequest.email())) {
                User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Not found user with id " + userId));
                user.setFirstName(userRequest.firstName());
                user.setLastName(userRequest.lastName());
                user.setEmail(userRequest.email());
                user.setPassword(passwordEncoder.encode(userRequest.password()));
                user.setDateOfBirth(userRequest.dateOfBirth());
                user.setRole(userRequest.role());
                user.setBlock(userRequest.isBlock());
                user.setPhoneNumber(userRequest.phoneNumber());
                log.info("Success updated!!!");
            }else throw new AlreadyExistsException("This email: "+userRequest.email()+" is already exists!");
        }
            return SimpleResponse.builder()
                    .message("Success updated!!!")
                    .httpStatus(HttpStatus.OK)
                    .build();
    }

    @Override
    public SimpleResponse delete(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Not found user with id " + userId));
        for (House house : user.getHouses()) {
            if (house.isBooked()){
                throw new BadRequestException("User's booking isn't finished!");
            }
        }
        userRepository.deleteById(userId);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Success deleted!")
                .build();
    }
}
