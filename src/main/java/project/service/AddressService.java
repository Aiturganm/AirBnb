package project.service;

import project.dto.request.AddressRequest;
import project.dto.response.HouseResponse;
import project.dto.response.SimpleResponse;
import project.enums.Region;

import java.util.List;

public interface AddressService {
    SimpleResponse save(Long houseId, Region region, AddressRequest addressRequest);

    List<HouseResponse> getRegionHouses(Region region);

    SimpleResponse update(Long addressId);
}
