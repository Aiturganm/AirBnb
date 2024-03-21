package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entities.Address;
import project.entities.House;

public interface HouseRepository extends JpaRepository<House, Long> {
}
