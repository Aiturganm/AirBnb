package project.api;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import project.service.RentInfoService;

@RestController
@RequestMapping("/api/rent-info")
@RequiredArgsConstructor
public class RentInfoApi {
    private final RentInfoService rentInfoService;

}
