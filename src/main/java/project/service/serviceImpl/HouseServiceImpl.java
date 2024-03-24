package project.service.serviceImpl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import project.config.jwt.JwtService;
import project.dto.request.HouseRequest;
import project.dto.response.HouseResponse;
import project.dto.response.SimpleResponse;
import project.dto.response.UserHouseResponse;
import project.entities.House;
import project.entities.User;
import project.enums.HouseType;
import project.enums.Region;
import project.enums.Role;
import project.exception.NotFoundException;
import project.repository.HouseRepository;
import project.repository.UserRepository;
import project.service.HouseService;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HouseServiceImpl implements HouseService {
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @PostConstruct
    public UserResponse initUser() {
        User user = new User("admin", "admin", "admin@gmail.com", passwordEncoder.encode("1234"), LocalDate.of(2020, 12, 12), Role.ADMIN, true, "njdkmvscl");
        userRepository.save(user);
        return UserResponse.builder()
                .token(jwtService.createToken(user))
                .email(user.getEmail())
                .role(user.getRole())
                .httpStatus(HttpStatus.OK)

                .build();
    }


    @Override
    public SimpleResponse saveHouse(HouseRequest houseRequest, Principal principal) {
        House house = new House();
        String name = principal.getName();
        User byEmail = userRepository.getByEmail(name);
        house.setUser(byEmail);
        house.setHouseType(houseRequest.getHouseType());
        house.setDescription(houseRequest.getDescription());
        house.setRoom(houseRequest.getRoom());
        house.setImages(houseRequest.getImages());
        house.setPrice(houseRequest.getPrice());
        house.setGuests(houseRequest.getGuests());
        house.setNameOfHotel(houseRequest.getNameOfHotel());
        houseRepository.save(house);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("house with name " + house.getNameOfHotel() + " successfully saved")
                .build();
    }

    @Override
    public HouseResponse findbyId(Long houseId) {
        House house = houseRepository.findById(houseId).orElseThrow(() -> new NotFoundException("house not found"));

        return HouseResponse.builder()
                .id(house.getId())
                .description(house.getDescription())
                .houseType(house.getHouseType())
                .images(house.getImages())
                .room(house.getRoom())
                .guests(house.getGuests())
                .price(house.getPrice())
                .build();
    }

    @Override
    public List<HouseResponse> findAll() {
        List<House> all = houseRepository.findAll();
        List<HouseResponse> houseResponses = new ArrayList<>();

        for (House house : all) {
            if (house.isPublished()) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getGuests()));
            }
        }
        return houseResponses;
    }

    @Override
    public SimpleResponse updateHouse(HouseRequest houseRequest, Long houseId, Principal principal) {
        House house1 = houseRepository.findById(houseId).orElseThrow(() -> new NotFoundException("house not found"));
        String name = principal.getName();
        User user = userRepository.getByEmail(name);
        for (House house : user.getHouses()) {
            if (house1.getId().equals(house.getId()) || user.getRole().equals(Role.ADMIN)) {
                house.setHouseType(houseRequest.getHouseType());
                house.setDescription(houseRequest.getDescription());
                house.setRoom(houseRequest.getRoom());
                house.setImages(houseRequest.getImages());
                house.setPrice(houseRequest.getPrice());
                house.setGuests(houseRequest.getGuests());
                house.setNameOfHotel(houseRequest.getNameOfHotel());
                houseRepository.save(house1);
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("house with id " + house1.getHouseType() + " successfully updated")
                        .build();

            }
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.MULTI_STATUS)
                .message("you can not update this house")
                .build();
    }

    @Override
    public SimpleResponse deleteHouse(Long houseId, Principal principal) {
        House house1 = houseRepository.findById(houseId).orElseThrow(() -> new NotFoundException("house not found"));
        String name = principal.getName();
        User user = userRepository.getByEmail(name);
        for (House house : user.getHouses()) {
            if (house1.getId().equals(house.getId()) || user.getRole().equals(Role.ADMIN)) {
                houseRepository.delete(house1);
                SimpleResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("success deleted")
                        .build();
            }
        }

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.MULTI_STATUS)
                .message("you can not delete this house")
                .build();
    }

    @Override
    public HouseResponse findByName(String houseName) {
        House house = houseRepository.findByHouseName(houseName).orElseThrow(() -> new NotFoundException("house not found"));
        return HouseResponse.builder()
                .id(house.getId())
                .description(house.getDescription())
                .houseType(house.getHouseType())
                .images(house.getImages())
                .room(house.getRoom())
                .build();
    }

    @Override
    public List<UserHouseResponse> findByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("user not found"));
        List<UserHouseResponse> userHouseResponses = new ArrayList<>();
        for (House house : user.getHouses()) {
            userHouseResponses.add(new UserHouseResponse(user.getFirstName(), house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getGuests()));

        }
        return userHouseResponses;

    }

    @Override
    public List<HouseResponse> sortByPrice(String ascOrDesc) {
        List<HouseResponse> houseResponses = new ArrayList<>();
        if (ascOrDesc.equalsIgnoreCase("asc")) {
            List<House> houseAsc = houseRepository.sortAsc("asc");
            for (House house : houseAsc) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getGuests()));
            }
        } else if (ascOrDesc.equalsIgnoreCase("desc")) {
            List<House> houseDesc = houseRepository.sortDesc("desc");
            for (House house : houseDesc) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getGuests()));
            }
        }
        return houseResponses;
    }

    @Override
    public List<HouseResponse> betweenPrice(BigDecimal startPrice, BigDecimal finishPrice) {
        List<House> houses = houseRepository.betweenPrice(startPrice, finishPrice);
        List<HouseResponse> houseResponses = new ArrayList<>();
        for (House house : houses) {
            houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getGuests()));
        }
        return houseResponses;
    }

    @Override
    public List<HouseResponse> findByRegion(Region region) {
        if (Region.OSH.equals(region) || Region.CHYI.equals(region) || Region.BATKEN.equals(region) || Region.NARYN.equals(region) || Region.TALAS.equals(region) || Region.JALAL_ABAD.equals(region) || Region.YSSYK_KOL.equals(region)) {
            List<House> houses = houseRepository.findByRegion(region);
            List<HouseResponse> houseResponses = new ArrayList<>();
            for (House house : houses) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getGuests()));
            }
            return houseResponses;
        }
        throw new NotFoundException("not found");
    }

    @Override
    public List<HouseResponse> filterByType(HouseType type) {
        if (HouseType.HOUSE.equals(type)) {
            List<House> houses = houseRepository.filterByType(type);
            List<HouseResponse> houseResponses = new ArrayList<>();
            for (House house : houses) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getGuests()));

            }
            return houseResponses;
        } else if (HouseType.APARTMENT.equals(type)) {
            List<House> houses = houseRepository.filterByType(type);
            List<HouseResponse> houseResponses = new ArrayList<>();
            for (House house : houses) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getGuests()));

            }
            return houseResponses;

        }

        throw new NotFoundException("not found");
    }

    @Override
    public List<HouseResponse> notPublishedHouses() {
        List<House> all = houseRepository.findAll();
        List<HouseResponse> houseResponses = new ArrayList<>();

        for (House house : all) {
            if (!house.isPublished()) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getGuests()));
            }
        }
        return houseResponses;
    }
}
