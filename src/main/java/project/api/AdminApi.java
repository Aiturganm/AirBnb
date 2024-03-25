package project.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import project.dto.response.HouseResponse;
import project.repository.UserRepository;
import project.service.HouseService;
import project.service.UserService;

import java.util.List;

@RequiredArgsConstructor
@RequestMapping("/admin/api")
public class AdminApi {
    private final UserService userService;
    private final HouseService houseService;

    @GetMapping("/findAllHousesPublishedHouses")
    public List<HouseResponse> allHouses(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size){
        return null;
    }
}
