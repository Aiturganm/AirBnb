package project.service.serviceImpl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    public SimpleResponse saveHouse(HouseRequest houseRequest, Principal principal, HouseType houseType) {
        House house = new House();
        String name = principal.getName();
        User byEmail = userRepository.getByEmail(name);
        if (byEmail.getCard() == null) {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("you dont have any card's")
                    .build();
        } else
            house.setUser(byEmail);
        byEmail.getHouses().add(house);
        house.setHouseType(houseType);
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
        byte rating = houseRepository.rating();

        return HouseResponse.builder()
                .id(house.getId())
                .description(house.getDescription())
                .houseType(house.getHouseType())
                .images(house.getImages())
                .room(house.getRoom())
                .rating()
                .guests(house.getGuests())
                .price(house.getPrice())
                .build();
    }

    @Override
    public List<HouseResponse> findAllPublisged(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<House> allPublished = houseRepository.findAllPublished(pageable);
        List<HouseResponse> houseResponses = new ArrayList<>();

        for (House house : allPublished) {
            if (house.isPublished()) {
                byte rating = houseRepository.rating();
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), rating, house.getGuests()));
            }
        }
        return houseResponses;
    }

    @Override
    public SimpleResponse updateHouse(HouseRequest houseRequest, Long houseId, Principal principal, HouseType houseType) {
        House house1 = houseRepository.findById(houseId).orElseThrow(() -> new NotFoundException("house not found"));
        String name = principal.getName();
        User user = userRepository.getByEmail(name);
        for (House house : user.getHouses()) {
            if (house1.getId().equals(house.getId()) || user.getRole().equals(Role.ADMIN)) {
                house.setHouseType(houseType);
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
        byte rating = houseRepository.rating();
        return HouseResponse.builder()

                .id(house.getId())
                .description(house.getDescription())
                .houseType(house.getHouseType())
                .rating(rating)
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
    public List<HouseResponse> sortByPrice(String ascOrDesc, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        byte rating = houseRepository.rating();
        List<HouseResponse> houseResponses = new ArrayList<>();
        if (ascOrDesc.equalsIgnoreCase("asc")) {
            Page<House> houseAsc = houseRepository.sortAsc(pageable, "asc");
            for (House house : houseAsc) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), rating, house.getGuests()));
            }
        } else if (ascOrDesc.equalsIgnoreCase("desc")) {
            Page<House> houseDesc = houseRepository.sortDesc(pageable, "desc");
            for (House house : houseDesc) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), rating, house.getGuests()));
            }
        }
        return houseResponses;
    }

    @Override
    public List<HouseResponse> betweenPrice(BigDecimal startPrice, BigDecimal finishPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        byte rating = houseRepository.rating();
        Page<House> houses = houseRepository.betweenPrice(pageable, startPrice, finishPrice);
        List<HouseResponse> houseResponses = new ArrayList<>();
        for (House house : houses) {
            houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), rating, house.getGuests()));
        }
        return houseResponses;
    }

    @Override
    public List<HouseResponse> findByRegion(Region region, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        byte rating = houseRepository.rating();
        if (Region.OSH.equals(region) || Region.CHYI.equals(region) || Region.BATKEN.equals(region) || Region.NARYN.equals(region) || Region.TALAS.equals(region) || Region.JALAL_ABAD.equals(region) || Region.YSSYK_KOL.equals(region)) {
            Page<House> houses = houseRepository.findByRegion(pageable, region);
            List<HouseResponse> houseResponses = new ArrayList<>();
            for (House house : houses) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), rating, house.getGuests()));
            }
            return houseResponses;
        }
        throw new NotFoundException("not found");
    }

    @Override
    public List<HouseResponse> filterByType(HouseType type, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        byte rating = houseRepository.rating();
        if (HouseType.HOUSE.equals(type)) {
            Page<House> houses = houseRepository.findAllPublished(pageable);
            List<HouseResponse> houseResponses = new ArrayList<>();
            for (House house : houses) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), rating, house.getGuests()));

            }
            return houseResponses;
        } else if (HouseType.APARTMENT.equals(type)) {
            Page<House> houses = houseRepository.filterByType(pageable, type);
            List<HouseResponse> houseResponses = new ArrayList<>();
            for (House house : houses) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), rating, house.getGuests()));

            }
            return houseResponses;

        }

        throw new NotFoundException("not found");
    }

    @Override
    public List<HouseResponse> notPublishedHouses(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<House> allPublished = houseRepository.FindAllNotPublished(pageable);
        List<HouseResponse> houseResponses = new ArrayList<>();
        byte rating = houseRepository.rating();
        for (House house : allPublished) {
            if (!house.isPublished()) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), rating, house.getGuests()));
            }
        }
        return houseResponses;
    }

    @Override
    public List<HouseResponse> popularHouses(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<House> allPublished = houseRepository.popularHouses(pageable);
        List<HouseResponse> houseResponses = new ArrayList<>();
        byte rating = houseRepository.rating();
        for (House house : allPublished) {
            if (!house.isPublished()) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), rating, house.getGuests()));
            }
        }
        return houseResponses;
    }
}
