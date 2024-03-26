package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import project.dto.response.HouseResponsesClass;
import project.entities.Address;
import project.enums.HouseType;
import project.enums.Region;

import java.math.BigDecimal;
import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    @Query("select new project.dto.response.HouseResponsesClass(a.house.id,a.house.nameOfHotel,a.house.description," +
            "a.house.images,a.house.room,a.house.houseType,a.house.price,a.house.guests)" +
            " from Address a where a.region = :region")
    List<HouseResponsesClass> findByRegion(@Param("region") Region region);

    boolean existsByStreet(String street);

    @Query("select a from Address a where a.house.id = :houseId")
    Address findByHouseId(Long houseId);

    @Query("select a from Address a where a.street =:street")
    Address getByStreet(String street);
}
