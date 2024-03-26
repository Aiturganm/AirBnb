package project.repository;

import org.hibernate.annotations.processing.SQL;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.dto.response.RentResponse;
import project.entities.Address;
import project.entities.RentInfo;

import java.util.List;
import java.util.Optional;

public interface RentInfoRepository extends JpaRepository<RentInfo, Long> {

    @Query("select r from RentInfo r inner join House h on r.house.id = h.id inner join User u on h.user.id = u.id where u.id = :id")
    List<RentInfo> findAllByVendor(Long id);

    @Query("select r from RentInfo r inner join User u on r.user.id = u.id where u.id = :id")
    List<RentInfo> findAllByUser(Long id);

    @Query("select r from RentInfo r where r.house.id = :houseId")
    List<RentInfo> getAllByHouse(Long houseId);

}
