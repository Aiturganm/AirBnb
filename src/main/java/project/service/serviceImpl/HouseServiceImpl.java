package project.service.serviceImpl;

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
import project.dto.request.AddressRequest;
import project.dto.request.HouseRequest;
import project.dto.response.*;
import project.entities.*;
import project.enums.HouseType;
import project.enums.Region;
import project.enums.Role;
import project.exception.NotFoundException;
import project.repository.*;
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
    private final CardRepository cardRepository;
    private final AddressRepository addressRepository;
    private final RentInfoRepository rentInfoRepository;
    private final HouseRepository houseRepository;
    private final PasswordEncoder passwordEncoder;
    private final FeedBackRepository feedBackRepository;
    private final JwtService jwtService;

    //    @PostConstruct
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
    public SimpleResponse saveHouse(HouseRequest houseRequest, Principal principal, HouseType houseType, Region region) {
        House house = new House();
        String name = principal.getName();
        User byEmail = userRepository.getByEmail(name);
        if (byEmail.getCard() == null || byEmail.isBlock()) {
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.BAD_REQUEST)
                    .message("you dont have any card's or YOU BLOCKED WITH ADMIN")
                    .build();
        } else
            house.setUser(byEmail);
        byEmail.getHouses().add(house);
        house.setHouseType(houseType);
        double rating = 0;
        house.setRating(rating);
        house.setDescription(houseRequest.getDescription());
        house.setRoom(houseRequest.getRoom());
        house.setImages(houseRequest.getImages());
        house.setPrice(houseRequest.getPrice());
        house.setGuests(houseRequest.getGuests());
        house.setNameOfHotel(houseRequest.getNameOfHotel());
        AddressRequest addressRequest = houseRequest.getAddressRequest();
        Address check = addressRepository.getByStreet(addressRequest.street());
        if (check != null) {
            return SimpleResponse.builder().httpStatus(HttpStatus.CONFLICT).message("Street already exists!").build();
        }
        houseRepository.save(house);
        Address address = new Address(region, addressRequest.city(), addressRequest.street(), house);
        addressRepository.save(address);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("house with name " + house.getNameOfHotel() + " successfully saved")
                .build();
    }

    @Override
    public HouseFeedBackResponse findbyId(Long houseId) {
        House house = houseRepository.findById(houseId).orElseThrow(() -> new NotFoundException("house not found"));
        List<FeedBackResponse> feedBackResponses = new ArrayList<>();
        for (Feedback feedback : house.getFeedbacks()) {
            feedBackResponses.add(new FeedBackResponse(feedback.getComment(), feedback.getRating()));
        }
        return HouseFeedBackResponse.builder()
                .id(house.getId())
                .nameOfHotel(house.getNameOfHotel())
                .description(house.getDescription())
                .houseType(house.getHouseType())
                .images(house.getImages())
                .room(house.getRoom())
                .rating(house.getRating())
                .guests(house.getGuests())
                .price(house.getPrice())
                .feedBackResponses(feedBackResponses)
                .build();
    }

    @Override
    public PaginationResponse findAllPublished(int page, int size) {
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
        House findHouse = houseRepository.findById(houseId).orElseThrow(() -> new NotFoundException("house not found"));
        String emailCurrentUser = principal.getName();
        User currentUser = userRepository.findByEmail(emailCurrentUser).orElseThrow(() -> new NotFoundException("Email or token invalid!"));
        log.info("SIZE:  " + currentUser.getHouses().size());
        if (currentUser.getRole().equals(Role.ADMIN)) {
            findHouse.setDescription(houseRequest.getDescription());
            findHouse.setRoom(houseRequest.getRoom());
            findHouse.setHouseType(houseType);
            findHouse.setImages(houseRequest.getImages());
            findHouse.setPrice(houseRequest.getPrice());
            findHouse.setGuests(houseRequest.getGuests());
            findHouse.setNameOfHotel(houseRequest.getNameOfHotel());
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("house with id " + findHouse.getNameOfHotel() + " successfully updated")
                    .build();
        } else if (currentUser.getHouses().contains(findHouse)) {
            log.info("2-IF");
            findHouse.setDescription(houseRequest.getDescription());
            findHouse.setRoom(houseRequest.getRoom());
            findHouse.setHouseType(houseType);
            findHouse.setImages(houseRequest.getImages());
            findHouse.setPrice(houseRequest.getPrice());
            findHouse.setGuests(houseRequest.getGuests());
            findHouse.setNameOfHotel(houseRequest.getNameOfHotel());
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("house with id " + findHouse.getNameOfHotel() + " successfully updated")
                    .build();
        }
        return SimpleResponse.builder().httpStatus(HttpStatus.FORBIDDEN).message("You no can update this house!").build();
    }


    @Override
    @Transactional
    public SimpleResponse deleteHouse(Long houseId, Principal principal) {
        String emailCurrentUser = principal.getName();
        User currentUser = userRepository.getByEmail(emailCurrentUser);
        House findHouse = houseRepository.findById(houseId).orElseThrow(() -> new NotFoundException("House not found!" + houseId));
        if (currentUser.getHouses().contains(findHouse)) {
            findHouse.getFavorites().forEach(favorite -> favorite.getHouses().remove(findHouse));
            log.info("ONE SUCCESS");
            Address byHouseId = addressRepository.findByHouseId(findHouse.getId());
            if (byHouseId != null) {
                addressRepository.delete(byHouseId);
            }
            feedBackRepository.deleteAll(findHouse.getFeedbacks());
            if (!findHouse.getRentInfos().isEmpty()) {
                RentInfo lastRentInfo = findHouse.getRentInfos().getLast();
                if (lastRentInfo != null && lastRentInfo.getCheckOut().isAfter(LocalDate.now())) {
                    String clientEmail = lastRentInfo.getUser().getEmail();
                    Card clientCard = cardRepository.findByUserEmail(clientEmail);
                    String vendorEmail = findHouse.getUser().getEmail();
                    Card vendorCard = cardRepository.findByUserEmail(vendorEmail);
                    vendorCard.setMoney(vendorCard.getMoney().subtract(lastRentInfo.getTotalPrice()));
                    clientCard.setMoney(clientCard.getMoney().add(lastRentInfo.getTotalPrice()));
                }
            }
            rentInfoRepository.deleteAll(findHouse.getRentInfos());
            houseRepository.delete(findHouse);
            log.info("TWO SUCCESS");


            currentUser.getHouses().remove(findHouse);
            findHouse.setUser(null);
            houseRepository.deleteById(findHouse.getId());
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success deleted this houses").build();
        }
        return SimpleResponse.builder().httpStatus(HttpStatus.FORBIDDEN).message("You no can deleted this house!").build();
    }

    @Override
    public PaginationResponse findByName(String houseName, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<House> houses = houseRepository.findByHouseName(houseName, pageable);
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
    public PagiUserHouse findByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        List<UserHouseResponse> userHouseResponses = new ArrayList<>();
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
