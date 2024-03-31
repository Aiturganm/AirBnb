package project.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.RequiredTypes;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.response.HouseResponse;
import project.dto.response.PaginationResponse;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FavoriteServiceImpl implements FavoriteService {
    private final FavoriteRepository favoriteRepository;
    private final HouseRepository houseRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SimpleResponse save(Long houseId) {
        House house = houseRepository.findById(houseId).orElseThrow(() -> new NotFoundException("Not found  house with id " + houseId));
        if (!house.isPublished()) return SimpleResponse.builder().httpStatus(HttpStatus.FORBIDDEN).message("THIS HOUSE NO PUBLISHED!").build();
        String emailCurrentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.findByEmail(emailCurrentUser).orElseThrow(() -> new NotFoundException("Not found user with email " + emailCurrentUser));
        Favorite currentUsFav = favoriteRepository.findByUserEmail(currentUser.getEmail());
        if (!currentUsFav.getHouses().contains(house) && !house.getFavorites().contains(currentUsFav)) {
            currentUsFav.getHouses().add(house);
            house.getFavorites().add(currentUsFav);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Success liked this house!" + houseId)
                    .build();
        }
        currentUsFav.getHouses().remove(house);
        house.getFavorites().remove(currentUsFav);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Success Un liked this house!" + houseId)
                .build();
    }


    @Override
    public PaginationResponse getMy(int page, int size) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        Long id = favoriteRepository.findByUserEmail(name).getId();
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<House> allHousesByFavoriteId = houseRepository.findAllHousesByFavoriteId(pageable, id);
        List<House> content = allHousesByFavoriteId.getContent();
        List<HouseResponse> houseResponses = new ArrayList<>();
        for (House house : content) {
            houseResponses.add(house.convert());
        }
        return new PaginationResponse(allHousesByFavoriteId.getNumber() + 1, allHousesByFavoriteId.getTotalPages(), houseResponses);
    }

}
