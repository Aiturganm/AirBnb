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
import org.springframework.transaction.annotation.Transactional;
import project.config.jwt.JwtService;
import project.dto.request.HouseRequest;
import project.dto.response.*;
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
        byte rating = 0;
        house.setRating(rating);
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
                .rating(house.getRating())
                .guests(house.getGuests())
                .price(house.getPrice())
                .build();
    }

    @Override
    public PaginationResponse findAllPublisged(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<House> allPublished = houseRepository.findAllPublished(pageable);
        List<HouseResponse> houseResponses = new ArrayList<>();
        for (House house : allPublished) {
            if (house.isPublished()) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getRating(), house.getGuests()));
            }
        }
        return PaginationResponse.builder()
                .page(allPublished.getNumber() + 1)
                .size(allPublished.getTotalPages())
                .houseResponseList(houseResponses)
                .build();
    }

    @Override
    @Transactional
    public SimpleResponse updateHouse(HouseRequest houseRequest, Long houseId, Principal principal, HouseType houseType) {
        House house1 = houseRepository.findById(houseId).orElseThrow(() -> new NotFoundException("house not found"));
        String name = principal.getName();
        User user = userRepository.getByEmail(name);
        for (House house : user.getHouses()) {
            if (house1.getId().equals(house.getId()) || user.getRole().equals(Role.ADMIN)) {
                house.setDescription(houseRequest.getDescription());
                house.setRoom(houseRequest.getRoom());
                house.setImages(houseRequest.getImages());
                house.setPrice(houseRequest.getPrice());
                house.setGuests(houseRequest.getGuests());
                house.setNameOfHotel(houseRequest.getNameOfHotel());
                houseRepository.save(house1);
                return SimpleResponse.builder()
                        .httpStatus(HttpStatus.OK)
                        .message("house with id " + house1.getNameOfHotel() + " successfully updated")
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
        House house = houseRepository.findById(houseId)
                .orElseThrow(() -> new NotFoundException("House not found"));

        String name = principal.getName();
        User user = userRepository.getByEmail(name);

        if (user.getRole().equals(Role.ADMIN) || user.getHouses().stream().anyMatch(userHouse -> userHouse.getId().equals(house.getId()))) {
            user.getHouses().removeIf(userHouse -> userHouse.getId().equals(house.getId()));
            houseRepository.deleteById(house.getId());

            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Success: House deleted")
                    .build();
        } else {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.MULTI_STATUS)
                    .message("You cannot delete this house")
                    .build();
        }

    }

    @Override
    public HouseResponse findByName(String houseName) {
        House house = houseRepository.findByHouseName(houseName).orElseThrow(() -> new NotFoundException("house not found"));
        return HouseResponse.builder()
                .id(house.getId())
                .nameOfHotel(house.getNameOfHotel())
                .description(house.getDescription())
                .houseType(house.getHouseType())
                .rating(house.getRating())
                .images(house.getImages())
                .room(house.getRoom())
                .build();
    }

    @Override
    public PagiUserHouse findByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<UserHouseResponse> userHouseResponses = new ArrayList<>();

        // Call the repository method to find houses by user ID
        Page<House> houses = houseRepository.findAllUserHouse(userId, pageable);

        for (House house : houses) {
            userHouseResponses.add(new UserHouseResponse(house.getUser().getFirstName(), house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getGuests()));
        }

        return PagiUserHouse.builder()
                .page(houses.getNumber() + 1)
                .size(houses.getTotalPages())
                .houseResponseList(userHouseResponses)
                .build();
    }

    @Override
    public PaginationResponse sortByPrice(String ascOrDesc, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<HouseResponse> houseResponses = new ArrayList<>();
        if (ascOrDesc.equalsIgnoreCase("asc")) {
            Page<House> houseAsc = houseRepository.sortAsc(pageable);
            for (House house : houseAsc) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getRating(), house.getGuests()));
            }
            return PaginationResponse.builder()
                    .page(houseAsc.getNumber() + 1)
                    .size(houseAsc.getTotalPages())
                    .houseResponseList(houseResponses).build();
        } else if (ascOrDesc.equalsIgnoreCase("desc")) {
            Page<House> houseDesc = houseRepository.sortDesc(pageable);
            for (House house : houseDesc) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getRating(), house.getGuests()));
            }
            return PaginationResponse.builder()
                    .page(houseDesc.getNumber() + 1)
                    .size(houseDesc.getTotalPages())
                    .houseResponseList(houseResponses)
                    .build();
        } else {
            throw new NotFoundException("enter asc or desc");
        }
    }


    @Override
    public PaginationResponse betweenPrice(BigDecimal startPrice, BigDecimal finishPrice, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<House> houses = houseRepository.betweenPrice(pageable, startPrice, finishPrice);
        List<HouseResponse> houseResponses = new ArrayList<>();
        for (House house : houses) {
            houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getRating(), house.getGuests()));
        }
        return PaginationResponse.builder()
                .page(houses.getNumber() + 1)
                .size(houses.getTotalPages())
                .houseResponseList(houseResponses).build();
    }


    @Override
    public PaginationResponse findByRegion(Region region, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        if (Region.OSH.equals(region) || Region.CHYI.equals(region) || Region.BATKEN.equals(region) || Region.NARYN.equals(region) || Region.TALAS.equals(region) || Region.JALAL_ABAD.equals(region) || Region.YSSYK_KOL.equals(region)) {
            Page<House> houses = houseRepository.findByRegion(pageable, region);
            List<HouseResponse> houseResponses = new ArrayList<>();
            for (House house : houses) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getRating(), house.getGuests()));
            }
            return PaginationResponse.builder()
                    .page(houses.getNumber() + 1)
                    .size(houses.getTotalPages())
                    .houseResponseList(houseResponses)
                    .build();
        }
        throw new NotFoundException("not found");
    }

    @Override
    public PaginationResponse filterByType(HouseType type, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<House> houses = houseRepository.filterByType(type, pageable);
        List<HouseResponse> houseResponses = new ArrayList<>();
        for (House house : houses) {
            houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getRating(), house.getGuests()));
        }

        return PaginationResponse.builder()
                .page(houses.getNumber() + 1)
                .size(houses.getTotalPages())
                .houseResponseList(houseResponses)
                .build();
    }


    @Override
    public PaginationResponse notPublishedHouses(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<House> allPublished = houseRepository.FindAllNotPublished(pageable);
        List<HouseResponse> houseResponses = new ArrayList<>();
        for (House house : allPublished) {
            if (!house.isPublished()) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getRating(), house.getGuests()));
            }
        }
        return PaginationResponse.builder()
                .page(allPublished.getNumber() + 1)
                .size(allPublished.getTotalPages())
                .houseResponseList(houseResponses)
                .build();
    }

    @Override
    public PaginationResponse popularHouses(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<House> allPublished = houseRepository.popularHouses(pageable);
        List<HouseResponse> houseResponses = new ArrayList<>();
        for (House house : allPublished) {
            if (!house.isPublished()) {
                houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getRating(), house.getGuests()));
            }
        }
        return PaginationResponse.builder()
                .page(allPublished.getNumber() + 1)
                .size(allPublished.getTotalPages())
                .houseResponseList(houseResponses)
                .build();
    }

    @Override
    public PaginationResponse allHouses(int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<House> all = houseRepository.findAllHouses(pageable);
        List<HouseResponse> houseResponses = new ArrayList<>();
        for (House house : all) {

            houseResponses.add(new HouseResponse(house.getId(), house.getNameOfHotel(), house.getDescription(), house.getImages(), house.getRoom(), house.getHouseType(), house.getPrice(), house.getRating(), house.getGuests()));


        }
        return PaginationResponse.builder()
                .page(all.getNumber() + 1)
                .size(all.getTotalPages())
                .houseResponseList(houseResponses)
                .build();

    }
}
