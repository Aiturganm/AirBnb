package project.api;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.dto.request.HouseRequest;
import project.dto.response.HouseResponse;
import project.dto.response.SimpleResponse;

import project.service.HouseService;


import project.dto.response.UserHouseResponse;
import project.enums.HouseType;
import project.enums.Region;
import java.math.BigDecimal;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/house")
@RequiredArgsConstructor
public class HouseApi {
    private final HouseService houseService;


    @PostMapping("/saveHouse")
    public SimpleResponse saveHouse(@RequestBody @Valid HouseRequest houseRequest, Principal principal) {
        return houseService.saveHouse(houseRequest, principal);
    }

    @PermitAll
    @GetMapping("/getHouseById/{houseId}")
    public HouseResponse findById(@PathVariable Long houseId) {
        return houseService.findbyId(houseId);
    }


    @PermitAll
    @GetMapping("/findAllHousesPublishedHouses")
    public List<HouseResponse> allHouses() {
        return houseService.findAll();
    }

    @Secured("ADMIN")
    @GetMapping("/findAllHousesNotPublishedHouses")
    public List<HouseResponse> NotAllHouses() {
        return houseService.notPublishedHouses();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDER')")
    @PutMapping("/updateHouse/{houseId}")
    public SimpleResponse updateHouse(@RequestBody HouseRequest houseRequest, @PathVariable Long houseId, Principal principal) {
        return houseService.updateHouse(houseRequest, houseId, principal);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDER')")
    @GetMapping("/deleteHouse/{houseId}")
    public SimpleResponse deleteHouse(@PathVariable Long houseId, Principal principal) {
        return houseService.deleteHouse(houseId, principal);
    }

    @PermitAll
    @GetMapping("/getHouseByName/{houseName}")
    public HouseResponse findByName(@RequestParam String houseName) {
        return houseService.findByName(houseName);
    }

    @PermitAll
    @GetMapping("/getHouseByUserId/{userId}")
    public List<UserHouseResponse> findByUserId(@PathVariable Long userId) {
        return houseService.findByUserId(userId);
    }

    @PermitAll
    @GetMapping("/sortByPrice/{ascOrDesc}")
    public List<HouseResponse> sortByPrice(@RequestParam String ascOrDesc) {
        return houseService.sortByPrice(ascOrDesc);
    }

    @PermitAll
    @GetMapping("/sortByBetweenPrice/{startPrice}/{FinishPrice}")
    public List<HouseResponse> betweenPrice(@PathVariable BigDecimal startPrice, @PathVariable BigDecimal FinishPrice) {
        return houseService.betweenPrice(startPrice, FinishPrice);
    }

    @PermitAll
    @GetMapping("/getHouseByRegion/{region}")
    public List<HouseResponse> findByRegion(@RequestParam Region region) {
        return houseService.findByRegion(region);
    }

    @PermitAll
    @GetMapping("/filterByHomeType/{type}")
    public List<HouseResponse> filteByType(@RequestParam HouseType type) {
        return houseService.filterByType(type);
    }
}


