package project.service.serviceImpl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.request.FeedBackRequest;
import project.dto.response.FeedBackResponse;
import project.dto.response.SimpleResponse;
import project.entities.Feedback;
import project.entities.House;
import project.entities.User;
import project.enums.Role;
import project.exception.NotFoundException;
import project.repository.FeedBackRepository;
import project.repository.HouseRepository;
import project.repository.UserRepository;
import project.service.FeedBackService;

import java.security.Principal;

@Service
@RequiredArgsConstructor
@Slf4j
public class FeedBackServiceImpl implements FeedBackService {
    private final FeedBackRepository feedBackRepository;
    private final UserRepository userRepository;
    private final HouseRepository houseRepository;

    @Override
    public SimpleResponse saveFeedBack(FeedBackRequest feedBackRequest, Principal principal, Long houseId) {
        String name = principal.getName();
        User byEmail = userRepository.getByEmail(name);
        House house = houseRepository.findById(houseId).orElseThrow(() -> new NotFoundException("house not found"));
        Feedback feedback = new Feedback();
        feedback.setHouse(house);
        feedback.setUser(byEmail);
        feedback.setComment(feedBackRequest.getComm());
        feedback.setRating(feedBackRequest.getRating());
        feedback.setImages(feedBackRequest.getImages());
        feedBackRepository.save(feedback);
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("feedBack success saved")
                .build();
    }

    @Override
    public SimpleResponse delete(Long feedId, Principal principal) {
        String name = principal.getName();
        User byEmail = userRepository.getByEmail(name);

        Feedback feedback = feedBackRepository.findById(feedId).orElseThrow(() -> new NotFoundException("feed not found"));
        if (feedback.getUser().getId() == byEmail.getId() || byEmail.getRole().equals(Role.ADMIN)) {
            feedBackRepository.delete(feedback);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("feedBack success deleted")
                    .build();
        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("you cant delete this fred back")
                .build();
    }

    @Override
    public FeedBackResponse getFeedBack(Long feedId) {
        Feedback feedback = feedBackRepository.findById(feedId).orElseThrow(() -> new NotFoundException("feed not found"));
        FeedBackResponse feedBackResponse = new FeedBackResponse(feedback.getComment(), feedback.getRating());
        return feedBackResponse;
    }

    @Override  @Transactional
    public SimpleResponse updateFeed(FeedBackRequest feedBackRequest, Long feedId, Principal principal) {
        String name = principal.getName();
        User byEmail = userRepository.getByEmail(name);
        Feedback feedback = feedBackRepository.findById(feedId).orElseThrow(() -> new NotFoundException("feed not found"));
        if (feedback.getUser().getId() == byEmail.getId() || byEmail.getRole().equals(Role.ADMIN)) {

            feedback.setComment(feedBackRequest.getComm());
            feedback.setRating(feedBackRequest.getRating());
            feedback.setImages(feedBackRequest.getImages());
            feedBackRepository.save(feedback);
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("success updated")
                    .build();

        }
        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("you cant updated")
                .build();
    }
}
