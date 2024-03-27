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
    boolean existsByStreet(String street);
}
