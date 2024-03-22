package project.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.dto.request.HouseRequest;
import project.dto.response.HouseResponse;
import project.dto.response.SimpleResponse;
import project.dto.response.UserHouseResponse;
import project.entities.House;
import project.entities.User;
import project.enums.HouseType;
import project.enums.Region;
import project.enums.Role;
import project.repository.HouseRepository;
import project.repository.UserRepository;
import project.service.HouseService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HouseServiceImpl implements HouseService {
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;

    @Override
    public SimpleResponse saveHouse(HouseRequest houseRequest, Principal principal) {
        String name = principal.getName();
        User byEmail = userRepository.getByEmail(name);
        House house = new House();
        byEmail.getHouses().add(house);
        house.setHouseType(houseRequest.getHouseType());
        house.setDescription(houseRequest.getDescription());
        house.setRoom(houseRequest.getRoom());
        house.setImages(houseRequest.getImages());
        houseRepository.save(house);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("success")
                .build();
    }

    @Override
    public HouseResponse findbyId(Long houseId) {
        House house = houseRepository.findById(houseId).orElseThrow(() -> new RuntimeException());

        return HouseResponse.builder()
                .id(house.getId())
                .description(house.getDescription())
                .houseType(house.getHouseType())
                .images(house.getImages())
                .room(house.getRoom())
                .build();
    }

    @Override
    public List<HouseResponse> findAll() {
        List<House> all = houseRepository.findAll();
        List<HouseResponse> houseResponses = new ArrayList<>();
        for (House house : all) {
            houseResponses.add(new HouseResponse(house.getId(), house.getDescription(), house.getRoom(), house.getHouseType(), house.getImages()));
        }
        return houseResponses;
    }

    @Override
    public SimpleResponse updateHouse(HouseRequest houseRequest, Long houseId, Principal principal) {
        House house1 = houseRepository.findById(houseId).orElseThrow(() -> new RuntimeException());
        String name = principal.getName();
        User user = userRepository.getByEmail(name);
        for (House house : user.getHouses()) {
            if (house1.getId().equals(house.getId()) || user.getRole().equals(Role.ADMIN)) {
                house1.setDescription(houseRequest.getDescription());
                house1.setHouseType(houseRequest.getHouseType());
                house1.setRoom(houseRequest.getRoom());
                house1.setImages(houseRequest.getImages());
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
        House house1 = houseRepository.findById(houseId).orElseThrow(() -> new RuntimeException());
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
        House house = houseRepository.findByHouseName(houseName).orElseThrow(() -> new RuntimeException());
        return   HouseResponse.builder()
                .id(house.getId())
                .description(house.getDescription())
                .houseType(house.getHouseType())
                .images(house.getImages())
                .room(house.getRoom())
                .build();
    }

    @Override
    public List<UserHouseResponse> findByUserId(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException());
       List<UserHouseResponse> userHouseResponses= new ArrayList<>();
        for (House house : user.getHouses()) {
            userHouseResponses.add(new UserHouseResponse(user.getFirstName(),house.getId(), house.getDescription(), house.getRoom(),house.getHouseType() , house.getImages()));
        }
        return userHouseResponses;

    }
    
    @Override
    public List<HouseResponse> sortByPrice(String ascOrDesc) {
        List<House> houses = houseRepository.sortByPrice(ascOrDesc);
        List<HouseResponse>houseResponses = new ArrayList<>();
        for (House house : houses) {
            houseResponses.add(new HouseResponse(house.getId(), house.getDescription(), house.getRoom(), house.getHouseType(), house.getImages()));
        }
        return houseResponses;
    }

    @Override
    public List<HouseResponse> betweenPrice(BigDecimal startPrice, BigDecimal finishPrice) {
        List<House> houses = houseRepository.betweenPrice(startPrice, finishPrice);
        List<HouseResponse>houseResponses = new ArrayList<>();
        for (House house : houses) {
            houseResponses.add(new HouseResponse(house.getId(), house.getDescription(), house.getRoom(), house.getHouseType(), house.getImages()));
        }
        return houseResponses;
    }

    @Override
    public List<HouseResponse> findByRegion(Region region) {

        List<House> houses = houseRepository.findbyRegion(region);
        List<HouseResponse>houseResponses = new ArrayList<>();
        for (House house : houses) {
            houseResponses.add(new HouseResponse(house.getId(), house.getDescription(), house.getRoom(), house.getHouseType(), house.getImages()));
        }
        return houseResponses;
    }

    @Override
    public List<HouseResponse> filterByType(HouseType type) {
        List<House> houses = houseRepository.filterType(type);
        List<HouseResponse>houseResponses = new ArrayList<>();
        for (House house : houses) {
            houseResponses.add(new HouseResponse(house.getId(), house.getDescription(), house.getRoom(), house.getHouseType(), house.getImages()));
        }
        return houseResponses;
    }
}
