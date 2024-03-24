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
    SimpleResponse saveHouse(HouseRequest houseRequest, Principal principal, HouseType houseType);

    HouseResponse findbyId(Long houseId);

    List<HouseResponse> findAllPublisged(int page, int size);

    SimpleResponse updateHouse(HouseRequest houseRequest, Long houseId, Principal principal, HouseType houseType);

    SimpleResponse deleteHouse(Long houseId, Principal principal);

    HouseResponse findByName(String houseName);

    List<UserHouseResponse> findByUserId(Long userId);

    List<HouseResponse> sortByPrice(String ascOrDesc,int page, int size);

    List<HouseResponse> betweenPrice(BigDecimal startPrice, BigDecimal finishPrice,int page, int size);

    List<HouseResponse> findByRegion(Region region,int page, int size);

    List<HouseResponse> filterByType(HouseType type,int page, int size);

    List<HouseResponse> notPublishedHouses(int page, int size);

    List<HouseResponse> popularHouses(int page, int size);
}
