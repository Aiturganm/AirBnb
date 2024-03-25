package project.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import project.dto.response.SimpleResponse;
import project.entities.Favorite;
import project.entities.House;
import project.entities.User;
import project.exception.NotFoundException;
import project.repository.FavoriteRepository;
import project.repository.HouseRepository;
import project.repository.UserRepository;
import project.service.FavoriteService;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

    @Override
    public SimpleResponse save(Long houseId) {
        House house = houseRepository.findById(houseId).orElseThrow(() -> new NotFoundException("Not found  house with id " + houseId));
        Favorite favorite = new Favorite();
        favorite.setCreatedAt(LocalDate.now());
        favorite.getHouses().add(house);
        house.getFavorites().add(favorite);
        String emailCurrentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(emailCurrentUser).orElseThrow(() -> new NotFoundException("Not found user with email " + emailCurrentUser));
        favorite.setUser(user);
        favoriteRepository.save(favorite);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Success created favourite!")
                .build();
    }
}
