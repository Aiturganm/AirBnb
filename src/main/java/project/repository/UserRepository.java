package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import project.entities.User;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    @Query("select s from User s where s.email = :email")
    Optional<User> findByEmail(String email);

    @Query("select s from User s where s.email like :email")
    User getByEmail(String email);

    @Query("select case when count(u)>0 then true else false end from User u where u.email like :email")
    boolean existsByEmail(String email);

    @Query("select  u from User u where u.role !='ADMIN'")
    Page<User> findAllMy(Pageable pageable);
}
