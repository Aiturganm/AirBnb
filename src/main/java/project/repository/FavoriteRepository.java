package project.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import project.entities.Favorite;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {
    @Query("select f from Favorite f where f.user.email =:email")
    Favorite findByUserEmail(String email);
    @Query("SELECT f FROM Favorite f WHERE f.user.email = :emailCurrentUser")
    Page<Favorite> findAllFavoriteByUserEmail(Pageable pageRequest,String emailCurrentUser);

}
