package project.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.stereotype.Service;
import project.entities.Favorite;
import project.repository.FavoriteRepository;
import project.service.FavoriteService;
@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
}
