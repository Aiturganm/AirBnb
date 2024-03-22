package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.entities.Address;
import project.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select s from User s where s.email = :email")
    Optional<User> findByEmail(String email);
    @Query("select s from User s where s.email like :email")
    User getByEmail(String email);
//    @Query("select case when count(u)>0 then true else false end from User u where u.email like :email")
//    boolean existsByEmail(String email);
//    @Query("SELECT u FROM User u WHERE u.restaurant.id IS NULL")
//    List<User> findAllRequest();
}
