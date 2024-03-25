package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entities.Favorite;

public interface FavoriteRepository extends JpaRepository<Favorite,Long> {
}
