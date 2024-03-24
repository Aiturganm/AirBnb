package project.api;

import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import project.dto.request.FeedBackRequest;
import project.dto.request.HouseRequest;
import project.dto.response.CardRes;
import project.dto.response.FeedBackResponse;
import project.dto.response.SimpleResponse;
import project.enums.HouseType;
import project.service.FeedBackService;

import java.security.Principal;

@RestController
@RequestMapping("/api/feedBack")
@RequiredArgsConstructor
public class FeedBackApi {
    private final FeedBackService feedBackService;


    @PermitAll
    @PostMapping("/saveFeedBack/{houseId}")
    public SimpleResponse saveFeedback(@RequestBody @ Valid FeedBackRequest feedBackRequest, Principal principal, @PathVariable
    Long houseId) {
        return feedBackService.saveFeedBack(feedBackRequest, principal, houseId);
    }
    @GetMapping("/getFeedBackById/{feedId}")
    public FeedBackResponse getFeedback(@PathVariable Long feedId) {
        return feedBackService.getFeedBack(feedId);
    }
    @PreAuthorize("hasAnyAuthority('ADMIN', 'VENDER')")
    @PutMapping("/updateFeed/{feedId}")
    public SimpleResponse updateFeed(@RequestBody FeedBackRequest feedBackRequest, @PathVariable Long feedId, Principal principal){
        return feedBackService.updateFeed(feedBackRequest,feedId, principal);
    }


    @DeleteMapping("/deleteFeedBack/{feedId}")
    public SimpleResponse delete(@PathVariable Long feedId, Principal principal) {
        return feedBackService.delete(feedId, principal);
    }

}
