package project.repository;

import jakarta.persistence.ElementCollection;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.dto.response.HouseResponse;
import project.dto.response.HouseResponsesClass;
import project.entities.Address;
import project.enums.HouseType;
import project.enums.Region;

import java.math.BigDecimal;
import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
     boolean existsByStreet(String street);

     @Query("select new project.dto.response.HouseResponsesClass(a.house.id,a.house.nameOfHotel,a.house.description,a.house.images,a.house.room,a.house.houseType,a.house.price,a.house.guests) from Address a where a.region = :region")
     List<HouseResponsesClass> findByRegion(Region region);
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
