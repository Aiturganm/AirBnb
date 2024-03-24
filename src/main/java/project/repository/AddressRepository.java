package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.dto.response.HouseResponsesClass;
import project.entities.Address;
import project.enums.Region;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {

    List<HouseResponsesClass> findByRegion(Region region);

    boolean existsByStreet(String street);
}
