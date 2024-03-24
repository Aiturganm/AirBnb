package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import project.entities.Address;
public interface AddressRepository extends JpaRepository<Address, Long> {
}
