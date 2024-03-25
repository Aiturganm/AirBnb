package project.api;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import project.dto.request.RentRequest;
import project.dto.response.SimpleResponse;
import project.service.RentInfoService;

import java.security.Principal;

@RestController
@RequestMapping("/api/rent-info")
@RequiredArgsConstructor
public class RentInfoApi {
    private final RentInfoService rentInfoService;
//AAAAAAAAAAAAAAAAAAAA
    @Secured({"USER", "VENDOR"})
    @PostMapping("/save")
    public SimpleResponse createRent(@RequestBody @Valid RentRequest request, Principal principal){
        return rentInfoService.createRent(request, principal);
    }
}
