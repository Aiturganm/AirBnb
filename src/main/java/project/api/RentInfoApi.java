package project.api;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import project.dto.request.RentRequest;
import project.dto.response.RentResponse;
import project.dto.response.SimpleResponse;
import project.service.RentInfoService;

import java.security.Principal;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rent-info")
@RequiredArgsConstructor
public class RentInfoApi {
    private final RentInfoService rentInfoService;
    @Secured({"USER", "VENDOR"})
    @PostMapping("/save")
    @Operation(description = "This method allowed to USER and VENDOR")
    public SimpleResponse createRent(@RequestBody @Valid RentRequest request, Principal principal){
        return rentInfoService.createRent(request, principal);
    }


    @GetMapping("/rent_infos")
    @Operation(description = "АВТОРИЗАЦИЯДАН ӨТКӨНДӨ ADMIN БОЛУП КИРСЕ БАРДЫК ҮЙЛӨРДҮН АРЕНДАЛАРЫ, " +
            "VENDOR БОЛУП КИРСЕ ӨЗҮНҮН ГАНА ҮЙЛӨРҮНҮН АРЕНДАЛАРЫ, " +
            "USER БОЛУП КИРСЕ ӨЗҮ АЛГАН ГАНА АРЕНДАЛАР КӨРҮНӨТ.")
    public List<RentResponse> getAll(Principal principal){
        return rentInfoService.getAll(principal);
    }

    @Secured({"ADMIN", "VENDOR"})
    @GetMapping("/house-rent_infos")
    @Operation(description = "This method allowed to ADMIN and VENDOR")
    public List<RentResponse> getAllByHouse(@RequestParam Long houseId, Principal principal){
        return rentInfoService.getAllByHouse(houseId, principal);
    }

    @GetMapping("/rent_info-by-id")
    public RentResponse getByRentId(@RequestParam Long rentId){
        return rentInfoService.getByRentId(rentId);
    }

    @Secured({"USER", "VENDOR"})
    @PutMapping("/update-rent_info")
    public RentResponse updateRent(LocalDate checkOut, @RequestParam Long rentId, Principal principal){
        return rentInfoService.updateRent(checkOut, rentId, principal);
    }

    @Secured({"USER", "VENDOR"})
    @DeleteMapping("/delete")
    public SimpleResponse deleteRent(Long rentId, Principal principal){
        return rentInfoService.deleteRent(rentId, principal);
    }
}
