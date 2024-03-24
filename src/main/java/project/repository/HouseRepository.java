package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
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

    @Query("select s from House s where s.isPublished = true order by s.price asc")
    Page<House> sortAsc(Pageable pageable,String asc);

    @Query("select s from House s where s.isPublished = true order by s.price desc")
    Page<House> sortDesc(Pageable pageable,String desc);

    @Query("select s from House s where s.isPublished = true and s.price between :startPrice and :finishPrice ")
    Page<House> betweenPrice(Pageable pageable,BigDecimal startPrice, BigDecimal finishPrice);

    @Query("select s from House s inner join Address a on a.house.id = s.id where s.isPublished = true and a.region = :region")
    Page<House> findByRegion(Pageable pageable,Region region);

    @Query("select s from House s where s.isPublished = TRUE and s.houseType = :type")
    Page<House> filterByType(Pageable pageable,HouseType type);

    @Query("select s from House s where s.isPublished = TRUE")
    Page<House> findAllPublished(Pageable pageable);
    @Query("select s from House s where s.isPublished = true ")
    Page<House> FindAllNotPublished(Pageable pageable);

    @Query("select s from House s where s.isPublished = true and s.rating > 4")
    Page<House> popularHouses(Pageable pageable);
    @Query("select avg(s.rating) from Feedback s" )
    byte rating();
}
