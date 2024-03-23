package project.service;

import project.dto.request.HouseRequest;
import project.dto.response.HouseResponse;
import project.dto.response.SimpleResponse;
import project.dto.response.UserHouseResponse;
import project.enums.HouseType;
import project.enums.Region;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

public interface HouseService {
    SimpleResponse saveHouse(HouseRequest houseRequest, Principal principal);

    HouseResponse findbyId(Long houseId);

    List<HouseResponse> findAll();

    SimpleResponse updateHouse(HouseRequest houseRequest, Long houseId, Principal principal);

    SimpleResponse deleteHouse(Long houseId, Principal principal);

//    HouseResponse findByName(String houseName);
//
//    List<UserHouseResponse> findByUserId(Long userId);
//
//    List<HouseResponse> sortByPrice(String ascOrDesc);
//
//    List<HouseResponse> betweenPrice(BigDecimal startPrice, BigDecimal finishPrice);
//
//    List<HouseResponse> findByRegion(Region region);
//
//    List<HouseResponse> filterByType(HouseType type);
}
