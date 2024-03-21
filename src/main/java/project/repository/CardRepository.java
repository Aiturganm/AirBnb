package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entities.Address;
import project.entities.Card;

public interface CardRepository extends JpaRepository<Card, Long> {
}
