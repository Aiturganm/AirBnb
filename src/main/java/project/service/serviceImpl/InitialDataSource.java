package project.service.serviceImpl;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.entities.Address;
import project.entities.House;
import project.entities.User;
import project.enums.HouseType;
import project.enums.Region;
import project.enums.Role;
import project.repository.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;

@Service
@Slf4j
@RequiredArgsConstructor
public class InitialDataSource {
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;
    private final RentInfoRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final FeedBackRepository feedBackRepository;
    private final AddressRepository addressRepository;

    @PostConstruct
    @Transactional
    public void saveData() {
        User admin = userRepository.findByEmail("admin@gmail.com")
                .orElseGet(() -> new User("admin", "admin", "admin@gmail.com", passwordEncoder.encode("1234"), LocalDate.of(2000, 12, 12), Role.ADMIN, false, "+996777858585"));
        User vendor = userRepository.findByEmail("vendor@gmail.com")
                .orElseGet(() -> new User("vendor", "vendor", "vendor@gmail.com", passwordEncoder.encode("1234"), LocalDate.of(2000, 12, 12), Role.VENDOR, false, "+996777858585"));
        User client = userRepository.findByEmail("client@gmail.com")
                .orElseGet(() -> new User("client", "client", "client@gmail.com", passwordEncoder.encode("1234"), LocalDate.of(2000, 12, 12), Role.USER, false, "+996777858585"));
        House house = createHouse(vendor);
        houseRepository.save(house);
        vendor.getHouses().add(house);
        userRepository.save(vendor);
        userRepository.save(admin);
        userRepository.save(client);
        addressRepository.save(createAddress(house));
    }
    private Address createAddress(House house){
        Address address = new Address();
        address.setCity("Kyzyl-Kiya");
        address.setRegion(Region.OSH);
        address.setStreet("Apsamat Masalieva 123");
        address.setHouse(house);
        return address;
    }

    private House createHouse(User user) {
        House house = new House();
        house.setNameOfHotel("Hotel Paradise");
        house.setDescription("Luxurious hotel with stunning ocean views.");
        house.setImages(Arrays.asList("image1.jpg", "image2.jpg", "image3.jpg"));
        house.setRoom((byte) 20);
        house.setHouseType(HouseType.HOUSE);
        house.setPrice(BigDecimal.valueOf(250.00));
        house.setRating((byte) 5);
        house.setBooked(false);
        house.setGuests(2);
        house.setPublished(true);
        house.setBlock(false);
        house.setUser(user);
        return house;
    }

}
