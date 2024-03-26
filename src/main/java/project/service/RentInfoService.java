package project.service;

import project.dto.request.RentRequest;
import project.dto.response.RentResponse;
import project.dto.response.SimpleResponse;
import project.repository.RentInfoRepository;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

public interface RentInfoService{
    SimpleResponse createRent(RentRequest request, Principal principal);

    List<RentResponse> getAll(Principal principal);

    List<RentResponse> getAllByHouse(Long houseId, Principal principal);

    RentResponse getByRentId(Long rentId);

    RentResponse updateRent(LocalDate checkOut, Long rentId, Principal principal);

    SimpleResponse deleteRent(Long rentId, Principal principal);
}
