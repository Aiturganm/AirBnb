package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.entities.Address;
import project.entities.House;
import project.enums.HouseType;
import project.enums.Region;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface HouseRepository extends JpaRepository<House, Long> {
//    Optional<House> findByHouseName(String houseName);
//
//    List<House> sortByPrice(String ascOrDesc);
//
//    List<House> betweenPrice(BigDecimal startPrice, BigDecimal finishPrice);
//
//    List<House> findbyRegion(Region region);
//
//    List<House> filterType(HouseType type);
}
