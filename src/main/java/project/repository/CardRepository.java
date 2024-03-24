package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.entities.Address;
import project.entities.Card;

public interface CardRepository extends JpaRepository<Card, Long> {
    @Query("select c from Card c where c.carNumber = :carNumber")
    Card findByNumber(int carNumber);

    @Query("select c from Card c join User u on c.user.id = u.id where u.email =:currentUserEmail")
    Card findByUserEmail(String currentUserEmail);
}
