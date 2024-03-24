package project.service;

import project.dto.request.HouseRequest;
import project.dto.response.*;


import project.enums.HouseType;
import project.enums.Region;

import java.math.BigDecimal;
import java.security.Principal;

public interface HouseService {
    SimpleResponse saveHouse(HouseRequest houseRequest, Principal principal, HouseType houseType);

    HouseResponse findbyId(Long houseId);

    PaginationResponse findAllPublisged(int page, int size);

    SimpleResponse updateHouse(HouseRequest houseRequest, Long houseId, Principal principal, HouseType houseType);

    SimpleResponse deleteHouse(Long houseId, Principal principal);

    HouseResponse findByName(String houseName);

    PagiUserHouse findByUserId(Long userId, int page, int size);

    PaginationResponse sortByPrice(String ascOrDesc,int page, int size);

    PaginationResponse betweenPrice(BigDecimal startPrice, BigDecimal finishPrice,int page, int size);

    PaginationResponse findByRegion(Region region,int page, int size);

    PaginationResponse filterByType(HouseType type,int page, int size);

    PaginationResponse notPublishedHouses(int page, int size);

    PaginationResponse popularHouses(int page, int size);

    PaginationResponse allHouses(int page, int size);
}
