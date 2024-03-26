package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.entities.Address;
import project.entities.Feedback;


public interface FeedBackRepository extends JpaRepository<Feedback, Long> {
    @Query("select f from Feedback f where f.house.id =:houseId")
    Page<Feedback> findAllByHouseId(Pageable pageable, Long houseId);
}
