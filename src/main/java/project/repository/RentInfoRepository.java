package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entities.Address;
import project.entities.RentInfo;

public interface RentInfoRepository extends JpaRepository<RentInfo, Long> {
}
