package project.api;

import jakarta.annotation.security.PermitAll;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.dto.request.HouseRequest;
import project.dto.response.*;
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

    @PermitAll
    @PostMapping("/saveHouse")
  
    public SimpleResponse saveHouse(@RequestBody HouseRequest houseRequest, Principal principal, @RequestParam HouseType houseType ) {
        return houseService.saveHouse(houseRequest , principal, houseType);
    }

    @PermitAll
    @GetMapping("/getHouseById/{houseId}")
    public HouseResponse findById(@PathVariable Long houseId) {
        return houseService.findbyId(houseId);
    }

    @PermitAll
    @GetMapping("/findAllHousesPublishedHouses")
    public PaginationResponse findAllPublishedHouses(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size){
        return houseService.findAllPublisged(page,size);
    }
    @Secured("ADMIN")
    @GetMapping("/findAllHouses")
    public PaginationResponse AllHouses(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return houseService.allHouses(page,size);
    }

    @Secured("ADMIN")
    @GetMapping("/findAllNotPublishedHouses")
    public PaginationResponse NotAllHouses(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return houseService.notPublishedHouses(page,size);    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDER')")
    @PutMapping("/updateHouse/{houseId}")
    public SimpleResponse updateHouse(@RequestBody HouseRequest houseRequest, @PathVariable Long houseId, Principal principal, @RequestParam HouseType houseType){
        return houseService.updateHouse(houseRequest, houseId, principal,houseType);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDER')")
    @GetMapping("/deleteHouse/{houseId}")
    public SimpleResponse deleteHouse(@PathVariable Long houseId, Principal principal) {
        return houseService.deleteHouse(houseId, principal);
    }

    @PermitAll
    @GetMapping("/getHouseByName")
    public PaginationResponse findByName(@RequestParam String houseName, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size) {
        return houseService.findByName(houseName, page, size);
    }

    @PermitAll
    @GetMapping("/getHouseByUserId/{userId}")
    public PagiUserHouse findByUserId(@PathVariable Long userId, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size) {
        return houseService.findByUserId(userId, page, size);
    }

    @PermitAll
    @GetMapping("/sortByPrice")
    public PaginationResponse sortByPrice(@RequestParam String ascOrDesc,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return houseService.sortByPrice(ascOrDesc,page,size);
    }

    @PermitAll
    @GetMapping("/sortByBetweenPrice/{startPrice}/{FinishPrice}")
    public PaginationResponse betweenPrice(@PathVariable BigDecimal startPrice, @PathVariable BigDecimal FinishPrice,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return houseService.betweenPrice(startPrice, FinishPrice,page,size);
    }

    @PermitAll
    @GetMapping("/getHouseByRegion")
    public PaginationResponse findByRegion(@RequestParam Region region,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return houseService.findByRegion(region,page,size);
    }

    @PermitAll
    @GetMapping("/filterByHomeType")
    public PaginationResponse filterByType(@RequestParam HouseType type,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return houseService.filterByType(type,page,size);
    }
    @PermitAll
    @GetMapping("/findPopular")
    public PaginationResponse popularsHouses(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return houseService.popularHouses(page,size);
    }

}


