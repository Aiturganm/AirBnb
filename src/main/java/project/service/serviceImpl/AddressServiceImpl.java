package project.service.serviceImpl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.dto.request.AddressRequest;
import project.dto.response.HouseResponsesClass;
import project.dto.response.SimpleResponse;
import project.entities.Address;
import project.entities.House;
import project.entities.User;
import project.enums.Region;
import project.exception.AlreadyExistsException;
import project.exception.NotFoundException;
import project.repository.AddressRepository;
import project.repository.HouseRepository;
import project.repository.UserRepository;
import project.service.AddressService;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SimpleResponse save(Long houseId, Region region, AddressRequest addressRequest) {
        House house = houseRepository.findById(houseId).orElseThrow(() -> new NotFoundException("Not found house with id " + houseId));
        for (Address address1 : addressRepository.findAll()) {
            if (!address1.getStreet().equals(addressRequest.street())) {
                Address address = new Address();
                address.setRegion(region);
                address.setCity(addressRequest.city());
                address.setStreet(addressRequest.street());
                address.setHouse(house);
                address.setHouse(house);
                addressRepository.save(address);
            } else throw new AlreadyExistsException("Street must be unique!");
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
    @Transactional
    public SimpleResponse update(Long houseId, Region region, AddressRequest addressRequest) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.getByEmail(email);
        House house = houseRepository.findById(houseId).orElseThrow(() -> new NotFoundException("House with id not found!" + houseId));
        if (!user.getHouses().contains(house)) return SimpleResponse.builder().httpStatus(HttpStatus.FORBIDDEN).
                message("You no  can update this house! " + houseId).build();
        Address address = addressRepository.findByHouseId(houseId);
        Address byStreet = addressRepository.getByStreet(addressRequest.street());
        if (byStreet != null)
            return SimpleResponse.builder().httpStatus(HttpStatus.CONFLICT).message("Street need uniq").build();
        address.setRegion(region);
        address.setCity(addressRequest.city());
        address.setStreet(addressRequest.street());
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success updated").build();
    }
}

