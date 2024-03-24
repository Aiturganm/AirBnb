package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entities.Address;
import project.entities.Feedback;


public interface FeedBackRepository extends JpaRepository<Feedback, Long> {
}
