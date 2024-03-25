package project.api;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import project.dto.request.AcceptOrRejectReq;
import project.dto.response.*;
import project.enums.ActionForHouse;
import project.enums.BlockOrUnBlock;
import project.enums.HouseType;
import project.enums.Region;
import project.service.HouseService;
import project.service.UserService;
import project.service.serviceImpl.AdminService;

import java.security.Principal;

@RequiredArgsConstructor
@RestController
@RequestMapping("/admin/api")
public class AdminApi {
    private final UserService userService;
    private final HouseService houseService;
    private final AdminService adminService;

    @Secured("ADMIN")
    @GetMapping("/getAllPublicHouse")
    public PaginationResponse allPublicHouses(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size) {
        return houseService.findAllPublished(page, size);
    }


    @Secured("ADMIN")
    @GetMapping("/getAllHouses")
    public PaginationResponse allHouse(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size) {
        return houseService.allHouses(page, size);
    }

    @Secured("ADMIN")
    @PutMapping("/actionForHouse/{houseId}")
    public SimpleResponse acceptOrReject(@PathVariable Long houseId, @RequestParam ActionForHouse actionForHouse, @RequestBody AcceptOrRejectReq acceptOrRejectReq, Principal principal) {
        return adminService.acceptOrReject(houseId, actionForHouse, principal, acceptOrRejectReq);
    }

//    @Secured("ADMIN")
//    @PutMapping("/blockUnBlock/{houseId}")
//    public SimpleResponse blockUnBlock(@PathVariable Long houseId, @RequestParam BlockOrUnBlock blockOrUnBlock) {
//        return adminService.blockUnBlock(houseId, blockOrUnBlock);
//    }
//
//    @Secured("ADMIN")
//    @PutMapping("/vendorBlockOrUnblock/{userId}")
//    public SimpleResponse userBlockOrUnBlock(@PathVariable Long userId, @RequestParam BlockOrUnBlock blockOrUnBlock) {
//        return adminService.userBlockOrUnBlock(userId, blockOrUnBlock);
//    }
    @Secured("ADMIN")
    @GetMapping("/getAllUsers")
    public PaginationUserResponse getAllUsers(@RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size){
        return userService.findAll(page,size);
    }
    @Secured("ADMIN")
    @GetMapping("/filterByPrice")
    public PaginationResponse sortByPrice(@RequestParam String ascOrDesc,@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return houseService.sortByPrice(ascOrDesc,page,size);
    }
    @Secured("ADMIN")
    @GetMapping("/filterByRegion")
    public PaginationResponse findByRegion(@RequestParam Region region, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size){
        return houseService.findByRegion(region,page,size);
    }
    @Secured("ADMIN")
    @GetMapping("/filterByHomeType")
    public PaginationResponse filterByType(@RequestParam HouseType type, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "4") int size){
        return houseService.filterByType(type,page,size);
    }
    @Secured("ADMIN")
    @GetMapping("/findPopular")
    public PaginationResponse popularsHouses(@RequestParam(defaultValue = "1") int page,@RequestParam(defaultValue = "4") int size){
        return houseService.popularHouses(page,size);
    }

}
