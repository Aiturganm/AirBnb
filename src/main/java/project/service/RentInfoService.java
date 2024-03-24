package project.service;

import project.dto.request.RentRequest;
import project.dto.response.SimpleResponse;
import project.repository.RentInfoRepository;

import java.security.Principal;

public interface RentInfoService{
    SimpleResponse createRent(RentRequest request, Principal principal);
}
