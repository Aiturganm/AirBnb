package project.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import project.dto.request.AddressRequest;
import project.dto.response.HouseResponse;
import project.dto.response.HouseResponsesClass;
import project.dto.response.SimpleResponse;
import project.entities.Address;
import project.entities.House;
import project.enums.Region;
import project.exception.AlreadyExistsException;
import project.exception.NotFoundException;
import project.repository.AddressRepository;
import project.repository.HouseRepository;
import project.service.AddressService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final HouseRepository houseRepository;
    @Override @Transactional
    public SimpleResponse save(Long houseId, Region region, AddressRequest addressRequest) {
        House house = houseRepository.findById(houseId).orElseThrow(() -> new NotFoundException("Not found house with id " + houseId));
        for (Address address1 : addressRepository.findAll()) {
            if (!address1.getStreet().equals(addressRequest.street())){
                Address address = new Address();
                address.setRegion(region);
                address.setCity(addressRequest.city());
                address.setStreet(addressRequest.street());
                address.setHouse(house);
            }else throw new AlreadyExistsException("Street must be unique!");
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Saved address!")
                .build();
    }

    @Override
    public List<HouseResponsesClass> getRegionHouses(Region region) {
        return null;
                //addressRepository.findByRegion(region);
       }

    @Override
    public SimpleResponse update(Long addressId,Region region ,AddressRequest addressRequest) {
        Address address = addressRepository.findById(addressId).orElseThrow(() -> new NotFoundException("Not found address with id " + addressId));
       // addressRepository.existsByStreet(addressRequest.street());
        address.setRegion(region);
        address.setCity(addressRequest.city());
        address.setStreet(addressRequest.street());
        return null;
    }
}
