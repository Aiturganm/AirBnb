package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import project.dto.response.HouseResponse;
import project.entities.House;
import project.enums.HouseType;
import project.enums.Region;

import java.math.BigDecimal;
import java.util.Optional;

public interface HouseRepository extends JpaRepository<House, Long> {
    @Query("select s from House s where s.nameOfHotel like :houseName ")
    Optional<House> findByHouseName(String houseName);

    @Query("select s from House s where s.isPublished = false order by s.price asc")
    Page<House> sortAsc(Pageable pageable);

    //    @Query("select new project.dto.response.HouseResponse(s.id,s.nameOfHotel , s.description, s.images, s.room , s.houseType, s.price, s.rating, s.guests) from House s where s.isPublished = false order by s.price desc")
//    Page<HouseResponse> sortDesc(Pageable pageable, String desc);
    @Query("select s from House s where s.isPublished = false order by s.price desc")
    Page<House> sortDesc(Pageable pageable);

    @Query("select s from House s where s.isPublished = false and s.price between :startPrice and :finishPrice ")
    Page<House> betweenPrice(Pageable pageable, BigDecimal startPrice, BigDecimal finishPrice);

    @Query("select s from House s inner join Address a on a.house.id = s.id where s.isPublished = false and a.region = :region")
    Page<House> findByRegion(Pageable pageable, Region region);

    //    @Query("select s from House s where s.isPublished = false and s.houseType = :HOUSE")
//    Page<House> filterByType(Pageable pageable);
    @Query("select s from House s where s.isPublished = false and s.houseType = :type")
    Page<House> filterByType(@Param("type") HouseType type, Pageable pageable);


    @Query("select s from House s where s.isPublished = TRUE")
    Page<House> findAllPublished(Pageable pageable);

    @Query("select s from House s where s.isPublished = false")
    Page<House> FindAllNotPublished(Pageable pageable);

    @Query("select s from House s where s.isPublished = false and s.rating > 4")
    Page<House> popularHouses(Pageable pageable);

    @Query("select avg(s.rating) from Feedback s")
    byte rating();

    @Query("select s from House s join s.user u where u.id =:userId")
    Page<House> findAllUserHouse(Long userId, Pageable pageable);

    @Query("select s from House s")
    Page<House> findAllHouses(Pageable pageable);



}
