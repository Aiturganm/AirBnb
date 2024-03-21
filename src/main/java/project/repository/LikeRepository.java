package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entities.Address;
import project.entities.Like;

public interface LikeRepository extends JpaRepository<Like, Long> {
}
