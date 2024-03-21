package project.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.dto.request.HouseRequest;
import project.dto.response.HouseResponse;
import project.service.HouseService;

@RestController
@RequestMapping("/api/house")
@RequiredArgsConstructor
public class HouseApi {
    private final HouseService houseService;

    @PostMapping
    public HouseResponse saveHouse(@RequestBody @Valid HouseRequest houseRequest){
        return
    }
}
