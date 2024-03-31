package project.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.dto.request.RentRequest;
import project.dto.response.RentResponse;
import project.dto.response.SimpleResponse;
import project.entities.Card;
import project.entities.House;
import project.entities.RentInfo;
import project.entities.User;
import project.enums.Role;
import project.exception.BadRequestException;
import project.exception.ForbiddenException;
import project.exception.NotFoundException;
import project.repository.CardRepository;
import project.repository.HouseRepository;
import project.repository.RentInfoRepository;
import project.repository.UserRepository;
import project.service.RentInfoService;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RentInfoServiceImpl implements RentInfoService {
    private final RentInfoRepository rentInfoRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;
    private final CardRepository cardRepository;

    @Override
    public SimpleResponse createRent(RentRequest request, Principal principal) {
        String name = principal.getName();
        User user = userRepository.getByEmail(name);
        Long houseId = request.houseId();
        if (request.checkIn().isBefore(LocalDate.now())){
            throw new BadRequestException("Check in date in incorrect!");
        }
        House house = houseRepository.findById(houseId).orElseThrow(() ->
                new NotFoundException("House with id " + houseId + " not found"));

        if (house.isBlock())
            throw new ForbiddenException("This house was blocked from by admin! So you can't rent this house.");
        if (!house.isPublished()) throw new NotFoundException("This house not published!");
        if (house.isBooked()) {
            RentInfo rentInfo = house.getRentInfos().get(house.getRentInfos().size() - 1);
            if (!rentInfo.getCheckOut().isBefore(request.checkIn()))
                throw new BadRequestException("On " + rentInfo.getCheckOut() + " the house was rented by someone");
        }
        if (!request.checkOut().isAfter(request.checkIn()))
            throw new BadRequestException("Date of check out must be later than check in");
        int rentDay = request.checkOut().compareTo(request.checkIn());
        BigDecimal totalPrice = house.getPrice().multiply(BigDecimal.valueOf(rentDay));
        int result = cardRepository.findByUserEmail(user.getEmail()).getMoney().compareTo(totalPrice);
        if (result < 0) throw new BadRequestException("Your balance less then total price of rent");

        Card clientCard = cardRepository.findByUserEmail(user.getEmail());
        Card vendorCard = cardRepository.findByUserEmail(house.getUser().getEmail());

        BigDecimal subtract = clientCard.getMoney().subtract(totalPrice);
        BigDecimal add = vendorCard.getMoney().add(totalPrice);

        clientCard.setMoney(subtract);
        vendorCard.setMoney(add);

        house.setBooked(true);
        RentInfo rentInfo1 = new RentInfo();
        rentInfo1.setUser(user);
        rentInfo1.setHouse(house);
        rentInfo1.setCheckIn(request.checkIn());
        rentInfo1.setCheckOut(request.checkOut());
        rentInfo1.setTotalPrice(totalPrice);
        rentInfoRepository.save(rentInfo1);
        house.addRentInfo(rentInfo1);

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully rented house " + house.getNameOfHotel() + " with id " + house.getId() + " to client " + user.getUsername())
                .build();
    }

    @Override
    public List<RentResponse> getAll(Principal principal) {
        User personByEmail = userRepository.getByEmail(principal.getName());
        Role role = personByEmail.getRole();
        List<RentResponse> rentResponses = new ArrayList<>();
        if (role.equals(Role.ADMIN)) {
            List<RentInfo> all = rentInfoRepository.findAll();
            for (RentInfo rentInfo : all) {
                rentResponses.add(RentResponse.builder()
                        .userId(rentInfo.getUser().getId())
                        .houseId(rentInfo.getHouse().getId())
                        .checkIn_checkOut("House rented from " + rentInfo.getCheckIn() + " until " + rentInfo.getCheckOut())
                        .totalPrice(rentInfo.getTotalPrice())
                        .build());
            }
        }

        if (role.equals(Role.VENDOR)) {
            List<RentInfo> allByVendor = rentInfoRepository.findAllByVendor(personByEmail.getId());
            for (RentInfo rentInfo : allByVendor) {
                rentResponses.add(RentResponse.builder()
                        .userId(rentInfo.getUser().getId())
                        .houseId(rentInfo.getHouse().getId())
                        .checkIn_checkOut("House rented from " + rentInfo.getCheckIn() + " until " + rentInfo.getCheckOut())
                        .totalPrice(rentInfo.getTotalPrice())
                        .build());
            }
        }

        if (role.equals(Role.USER)) {
            List<RentInfo> allByUser = rentInfoRepository.findAllByUser(personByEmail.getId());
            for (RentInfo rentInfo : allByUser) {
                rentResponses.add(RentResponse.builder()
                        .userId(rentInfo.getUser().getId())
                        .houseId(rentInfo.getHouse().getId())
                        .checkIn_checkOut("House rented from " + rentInfo.getCheckIn() + " until " + rentInfo.getCheckOut())
                        .totalPrice(rentInfo.getTotalPrice())
                        .build());
            }
        }

        return rentResponses;
    }

    @Override
    public List<RentResponse> getAllByHouse(Long houseId, Principal principal) {
        houseRepository.findById(houseId).orElseThrow(() -> new NotFoundException("House with id " + houseId + " not found."));
        String name = principal.getName();
        User userByEmail = userRepository.getByEmail(name);
        List<RentResponse> rentResponses = new ArrayList<>();
        if (userByEmail.getRole().equals(Role.USER))
            throw new BadRequestException("User can't get information about all houses!");
        if (userByEmail.getRole().equals(Role.ADMIN)) {
            List<RentInfo> allByHouse = rentInfoRepository.getAllByHouse(houseId);
            for (RentInfo rentInfo : allByHouse) {
                rentResponses.add(RentResponse.builder()
                        .houseId(rentInfo.getHouse().getId())
                        .userId(rentInfo.getUser().getId())
                        .checkIn_checkOut("House rented from " + rentInfo.getCheckIn() + " until " + rentInfo.getCheckOut())
                        .totalPrice(rentInfo.getTotalPrice())
                        .build());
            }
        }

        boolean isHouse = false;
        for (House house : userByEmail.getHouses()) {
            if (house.getId().equals(houseId)) {
                isHouse = true;
                break;
            }
        }

        if (userByEmail.getRole().equals(Role.VENDOR)) {
            if (isHouse) {
                List<RentInfo> allByHouse = rentInfoRepository.getAllByHouse(houseId);
                for (RentInfo rentInfo : allByHouse) {
                    rentResponses.add(RentResponse.builder()
                            .houseId(rentInfo.getHouse().getId())
                            .userId(rentInfo.getUser().getId())
                            .checkIn_checkOut("House rented from " + rentInfo.getCheckIn() + " until " + rentInfo.getCheckOut())
                            .totalPrice(rentInfo.getTotalPrice())
                            .build());
                }
            } else throw new NotFoundException(userByEmail.getEmail() + " doesn't have a house with id " + houseId);
        }
        return rentResponses;
    }

    @Override
    public RentResponse getByRentId(Long rentId) {
        RentInfo rentInfo = rentInfoRepository.findById(rentId).orElseThrow(() -> new NotFoundException("Rent info with id " + rentId + " not found."));
        return RentResponse.builder()
                .userId(rentInfo.getUser().getId())
                .houseId(rentInfo.getHouse().getId())
                .checkIn_checkOut("House rented from " + rentInfo.getCheckIn() + " until " + rentInfo.getCheckOut())
                .totalPrice(rentInfo.getTotalPrice())
                .build();
    }

    @Override
    public RentResponse updateRent(LocalDate checkOut, Long rentId, Principal principal) {
        User userByEmail = userRepository.getByEmail(principal.getName());
        RentInfo rentInfo = rentInfoRepository.findById(rentId).orElseThrow(() -> new NotFoundException("Rent info with id " + rentId + " not found."));
        if (rentInfo.getUser().getId().equals(userByEmail.getId())) {
            int rentDay = checkOut.compareTo(rentInfo.getCheckOut());
            BigDecimal totalPrice = rentInfo.getHouse().getPrice().multiply(BigDecimal.valueOf(rentDay).abs());

            int result = cardRepository.findByUserEmail(userByEmail.getEmail()).getMoney().compareTo(totalPrice);

            if (result < 0) throw new BadRequestException("Your balance less then total price of rent");

            Card clientCard = cardRepository.findByUserEmail(userByEmail.getEmail());
            Card vendorCard = cardRepository.findByUserEmail(rentInfo.getHouse().getUser().getEmail());

            BigDecimal subtract = clientCard.getMoney().subtract(totalPrice);
            BigDecimal add = vendorCard.getMoney().add(totalPrice);
            rentInfo.setCheckOut(checkOut);

            clientCard.setMoney(subtract);
            vendorCard.setMoney(add);
            rentInfo.setTotalPrice(totalPrice);
        }else throw new ForbiddenException("You were not rented this house.");

        return RentResponse.builder()
                .userId(userByEmail.getId())
                .houseId(rentInfo.getHouse().getId())
                .checkIn_checkOut("House rented from " + rentInfo.getCheckIn() + " until " + rentInfo.getCheckOut())
                .totalPrice(rentInfo.getTotalPrice())
                .build();
    }

    @Override
    public SimpleResponse deleteRent(Long rentId, Principal principal) {
        User userByEmail = userRepository.getByEmail(principal.getName());
        RentInfo rentInfo = rentInfoRepository.findById(rentId).orElseThrow(() -> new NotFoundException("Rent info with id " + rentId + " not found."));
        if (rentInfo.getUser().getId().equals(userByEmail.getId())) {
            rentInfoRepository.delete(rentInfo);
        }else throw new ForbiddenException("You were not rented this house.");

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully deleted!")
                .build();
    }


}
