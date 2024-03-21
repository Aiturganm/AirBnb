package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entities.Address;
import project.entities.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
