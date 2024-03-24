package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.dto.response.HouseResponse;
import project.entities.Address;
import project.enums.Region;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
     @Query("SELECT NEW project.dto.response.HouseResponse(h.id, h.nameOfHotel, h.description,h.images,h.room,h.houseType,h.price,h.guests) FROM House h WHERE h.region = :region")
     List<HouseResponse> getRegionHouses(Region region);
//      String nameOfHotel,
//        String description,
//        @ElementCollection
//         List<String> images,
//         byte room,
//        @Enumerated(EnumType.STRING)
//         HouseType houseType,
//         BigDecimal price,
//         int guests
}
