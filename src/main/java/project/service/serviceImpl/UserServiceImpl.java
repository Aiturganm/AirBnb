package project.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.config.jwt.JwtService;
import project.dto.request.SignInRequest;
import project.dto.request.SignUpRequest;
import project.dto.response.UserResponse;
import project.dto.response.*;
import project.entities.*;
import project.enums.Role;
import project.exception.AlreadyExistsException;
import project.exception.ForbiddenException;
import project.exception.NotFoundException;
import project.repository.*;
import project.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RentInfoRepository rentInfoRepository;
    private final CardRepository cardRepository;
    private final FavoriteRepository favoriteRepository;
    private final FeedBackRepository feedBackRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final HouseRepository houseRepository;
    private final AddressRepository addressRepository;

    @Override
    @Transactional
    public SimpleResponse signUp(SignUpRequest signUpRequest) {
        boolean exist = userRepository.existsByEmail(signUpRequest.getEmail());
        if (exist) throw new AlreadyExistsException("Email already exists!!!");
        User user = new User();
        user.setDateOfBirth(signUpRequest.getDateOfBirth());
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        user.setRole(Role.USER);
        userRepository.save(user);
        Favorite favorite = new Favorite(LocalDate.now(), user, new ArrayList<>());
        favoriteRepository.save(favorite);
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
        boolean matches = passwordEncoder.matches(decodePassword, password);
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
        Page<User> usersPage = userRepository.findAllMy(pageable);
        List<User> content = usersPage.getContent();
        List<UserResponse> responses = new ArrayList<>();
        for (User u : content) {
            responses.add(new UserResponse(u.getFirstName(), u.getLastName(),
                    u.getEmail(), u.getDateOfBirth(), u.getRole(), u.isBlock(), u.getPhoneNumber()));
        }
        return new PaginationUserResponse(usersPage.getNumber() + 1, usersPage.getTotalPages(), responses);

    }

    @Override
    @Transactional
    public SimpleResponse update(SignUpRequest signUpRequest) {
        User user = userRepository.getByEmail(SecurityContextHolder.getContext().getAuthentication().getName());
        boolean exist = userRepository.existsByEmail(signUpRequest.getEmail());
        if (exist) throw new AlreadyExistsException("Email already exists!!!");
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        user.setDateOfBirth(signUpRequest.getDateOfBirth());
        user.setPhoneNumber(signUpRequest.getPhoneNumber());
        log.info("Success updated!!!");
        String token = jwtService.createToken(user);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("YOUR NEW TOKEN!" + token).build();
    }

    @Override
    @Transactional
    public SimpleResponse delete() {
        String emailCurrentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.getByEmail(emailCurrentUser);
        if (currentUser.getRole().equals(Role.USER)) {
            List<Feedback> feedbacks = feedBackRepository.findByUserId(currentUser.getId());
            feedbacks.forEach(feedback -> feedback.setUser(null));
            Favorite favorite = favoriteRepository.findByUserEmail(currentUser.getEmail());
            favorite.setHouses(new ArrayList<>());
            favoriteRepository.delete(favorite);
            List<RentInfo> allByUser = rentInfoRepository.findAllByUser(currentUser.getId());
            rentInfoRepository.deleteAll(allByUser);
            userRepository.delete(currentUser);
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success deleted!").build();
        }
        return deleteVendor(currentUser);
    }

    @Override
    public UserResponse findById(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("Not found User with id " + userId));
//        User user = userRepository.getReferenceById(userId);
        return UserResponse.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .dateOfBirth(user.getDateOfBirth())
                .role(user.getRole())
                .isBlock(user.isBlock())
                .phoneNumber(user.getPhoneNumber())
                .build();
    }

    @Override
    public UserResForAdmin findByIdForAdmin(Long userId) {
        UserResForAdmin resForAdmin = userRepository.
                findById(userId).orElseThrow(() -> new NotFoundException("Not found user with id" + userId)).
                convert();
        List<HouseResForAdmin> allHouseByUserId = houseRepository.findAllHouseByUserId(userId);
        log.info("RES SIZE:" + allHouseByUserId.size());
        resForAdmin.setHouseResForAdminList(allHouseByUserId);
        return resForAdmin;

    }

    @Transactional
    public SimpleResponse deleteVendor(User user) {
        for (int i = 0; i < user.getHouses().size(); i++) {
            House house = houseRepository.findById(user.getHouses().get(i).getId()).get();
            house.getFavorites().forEach(favorite -> favorite.getHouses().remove(house));
            log.info("ONE SUCCESS");
            Address byHouseId = addressRepository.findByHouseId(house.getId());
            if (byHouseId != null) {
                addressRepository.delete(byHouseId);
            }
            feedBackRepository.deleteAll(house.getFeedbacks());
            if (!house.getRentInfos().isEmpty()) {
                RentInfo lastRentInfo = house.getRentInfos().getLast();
                if (lastRentInfo != null && lastRentInfo.getCheckOut().isAfter(LocalDate.now())) {
                    String clientEmail = lastRentInfo.getUser().getEmail();
                    Card clientCard = cardRepository.findByUserEmail(clientEmail);
                    String vendorEmail = house.getUser().getEmail();
                    Card vendorCard = cardRepository.findByUserEmail(vendorEmail);
                    vendorCard.setMoney(vendorCard.getMoney().subtract(lastRentInfo.getTotalPrice()));
                    clientCard.setMoney(clientCard.getMoney().add(lastRentInfo.getTotalPrice()));
                }
            }
            rentInfoRepository.deleteAll(house.getRentInfos());
            houseRepository.delete(house);
            log.info("TWO SUCCESS");
        }
        Favorite favorite = favoriteRepository.findByUserEmail(user.getEmail());
        favorite.setHouses(null);
        favoriteRepository.delete(favorite);
        log.info("THREE SUCCESS");
        userRepository.delete(user);
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("SUCCESS").build();

    }

}
