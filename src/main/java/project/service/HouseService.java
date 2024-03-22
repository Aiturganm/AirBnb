package project.service;

import project.dto.request.HouseRequest;
import project.dto.response.HouseResponse;
import project.dto.response.SimpleResponse;

import java.security.Principal;
import java.util.List;

public interface HouseService {
    SimpleResponse saveHouse(HouseRequest houseRequest, Principal principal);

    HouseResponse findbyId(Long houseId);

    List<HouseResponse> findAll();

    SimpleResponse updateHouse(HouseRequest houseRequest, Long houseId, Principal principal);

    SimpleResponse deleteHouse(Long houseId, Principal principal);

    HouseResponse findByName(String houseName);
}
