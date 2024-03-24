package project.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import project.dto.request.AddressRequest;
import project.dto.response.HouseResponse;
import project.dto.response.SimpleResponse;
import project.enums.Region;
import project.service.AddressService;

import java.util.List;

@RestController
@RequestMapping("/api/address")
@RequiredArgsConstructor
public class AddressApi {
    private final AddressService addressService;

    @PostMapping("save/{houseId}")
    public SimpleResponse saveAddress(@PathVariable Long houseId,
                                      @RequestParam Region region,
                                      @RequestBody AddressRequest addressRequest){
        return addressService.save(houseId,region,addressRequest);
    }
    @GetMapping("/houses")
    public List<HouseResponse> getRegionHouses(@RequestParam Region region){
        return addressService.getRegionHouses(region);
    }

    @PutMapping("/{addressId}")
    public SimpleResponse update(@PathVariable Long addressId){
        return addressService.update(addressId);
    }
}
