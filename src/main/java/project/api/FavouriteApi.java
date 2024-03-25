package project.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dto.response.SimpleResponse;
import project.service.FavoriteService;

@RestController
@RequestMapping("/api/favourite")
@RequiredArgsConstructor
public class FavouriteApi {
    private final FavoriteService favoriteService;

    @PostMapping("save/{houseId}")
    public SimpleResponse saveFav(@PathVariable Long houseId){
        return null;
    }

}
