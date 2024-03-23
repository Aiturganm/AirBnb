package project.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.dto.request.HouseRequest;
import project.dto.response.HouseResponse;
import project.dto.response.SimpleResponse;
import project.dto.response.UserHouseResponse;
import project.enums.HouseType;
import project.enums.Region;
import project.service.HouseService;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/house")
@RequiredArgsConstructor
public class HouseApi {
    private final HouseService houseService;
    @PostMapping("/saveHouse")
    public SimpleResponse saveHouse(@RequestBody @Valid HouseRequest houseRequest, Principal principal){
        return houseService.saveHouse(houseRequest, principal);
    }
    @GetMapping("/getHouseById/{houseId}")
    public HouseResponse findById(@PathVariable Long houseId){
        return houseService.findbyId(houseId);
    }
    @GetMapping("/findAllHouses")
    public List<HouseResponse> allHouses(){
        return houseService.findAll();
    }
    @PutMapping("/updateHouse/{houseId}")
    public SimpleResponse updateHouse(@RequestBody HouseRequest houseRequest, @PathVariable Long houseId, Principal principal){
        return houseService.updateHouse(houseRequest, houseId, principal);
    }
    @GetMapping("/deleteHouse/{houseId}")
    public SimpleResponse deleteHouse( @PathVariable Long houseId, Principal principal){
        return houseService.deleteHouse(houseId, principal);
    }
    @GetMapping("/getHouseByName/{houseName}")
    public HouseResponse findByName(@RequestParam String houseName){
        return houseService.findByName(houseName);
    }
    @GetMapping("/getHouseByUserId/{userId}")
    public List<UserHouseResponse> findByUserId(@PathVariable Long userId){
        return houseService.findByUserId(userId);
    }
    @GetMapping("/sortByPrice/{ascOrDesc}")
    public List<HouseResponse> sortByPrice(@RequestParam String ascOrDesc){
        return houseService.sortByPrice(ascOrDesc);
    }
    @GetMapping("/sortByBetweenPrice/{startPrice}/{FinishPrice}")
    public List<HouseResponse> betweenPrice(@PathVariable BigDecimal startPrice, @PathVariable BigDecimal FinishPrice){
        return houseService.betweenPrice(startPrice, FinishPrice);
    }
    @GetMapping("/getHouseByRegion/{region}")
    public List<HouseResponse> findByRegion(@RequestParam Region region){
        return houseService.findByRegion(region);
    }
    @GetMapping("/filterByHomeType/{type}")
    public List<HouseResponse> filteByType(@RequestParam HouseType type){
        return houseService.filterByType(type);
    }
}


