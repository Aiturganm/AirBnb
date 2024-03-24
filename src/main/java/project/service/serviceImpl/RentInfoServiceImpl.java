package project.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.dto.request.RentRequest;
import project.dto.response.SimpleResponse;
import project.entities.House;
import project.entities.RentInfo;
import project.entities.User;
import project.exception.AlreadyExistsException;
import project.exception.NotFoundException;
import project.repository.HouseRepository;
import project.repository.RentInfoRepository;
import project.repository.UserRepository;
import project.service.RentInfoService;

import java.math.BigDecimal;
import java.security.Principal;
import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RentInfoServiceImpl implements RentInfoService {
    private final RentInfoRepository rentInfoRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;

    @Override
    public SimpleResponse createRent(RentRequest request, Principal principal) {
        String name = principal.getName();
        User user = userRepository.getByEmail(name);
        Long houseId = request.houseId();
        House house = houseRepository.findById(houseId).orElseThrow(() ->
                new NotFoundException("House with id " + houseId + " not found"));

        boolean isAccept = false;
        if (!house.isBlock() && house.isPublished()) {
            for (RentInfo rentInfo : house.getRentInfos()) {
                if (rentInfo.getCheckOut().isBefore(request.checkIn()) && request.checkOut().isAfter(request.checkIn())) {
                    int rentDay = request.checkOut().compareTo(request.checkIn());
                    BigDecimal totalPrice = house.getPrice().multiply(BigDecimal.valueOf(rentDay));
                    int result = user.getCard().getMoney().compareTo(totalPrice);
                    if (result < 0)
                        return SimpleResponse.builder().httpStatus(HttpStatus.BAD_REQUEST).message("Your balance less then total price of rent").build();
                    BigDecimal subtract = user.getCard().getMoney().subtract(totalPrice);
                    BigDecimal add = house.getUser().getCard().getMoney().add(totalPrice);
                    RentInfo rentInfo1 = new RentInfo();
                    rentInfo1.setUser(user);
                    rentInfo1.setHouse(house);
                    rentInfo1.setCheckIn(request.checkIn());
                    rentInfo1.setCheckOut(request.checkOut());
                    rentInfo1.setTotalPrice(totalPrice);
                    rentInfoRepository.save(rentInfo1);
                    house.addRentInfo(rentInfo1);
                    isAccept = true;
                } else {
                    throw new AlreadyExistsException("This house not free for " + request.checkIn() + " day.");
                }
            }
        }
        if(isAccept){
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Successfully rented house " + house.getNameOfHotel() + " with id " + house.getId() + " to client " + user.getUsername())
                    .build();
        }
        else return SimpleResponse.builder()
                .httpStatus(HttpStatus.NOT_ACCEPTABLE)
                .message("Doesn't rented house")
                .build();
    }
}
