package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    @Query("select s from House s where s.nameOfHotel like :houseName ")
    Optional<House> findByHouseName(String houseName);

    @Query("select s from House s order by s.price asc")
    List<House> sortAsc(String asc);

    @Query("select s from House s order by s.price desc")
    List<House> sortDesc(String desc);

    @Query("select s from House s where s.price between :startPrice and :finishPrice")
    List<House> betweenPrice(BigDecimal startPrice, BigDecimal finishPrice);

    @Query("select s from House s inner join Address a on a.house.id = s.id where a.region = :region")
    List<House> findByRegion(Region region);

    @Query("select s from House s where s.houseType = :type")
    List<House> filterByType(HouseType type);

    @Query("select s from House s where s.isPublished = TRUE")
    Page<House> findAllPublished(Pageable pageable);
    @Query("select s from House s where s.isPublished = FALSE")
    Page<House> FindAllNotPublished(Pageable pageable);
}
