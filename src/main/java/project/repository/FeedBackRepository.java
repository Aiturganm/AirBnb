package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.entities.Address;
import project.entities.Feedback;

import java.util.List;


public interface FeedBackRepository extends JpaRepository<Feedback, Long> {
    @Query("select f from Feedback f where f.house.id =:houseId")
    Page<Feedback> findAllByHouseId(Pageable pageable, Long houseId);

    @Query("SELECT f from Feedback f where f.user.id = :id")
    List<Feedback> findByUserId(Long id);
}
