package project.service.serviceImpl;

import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.dto.request.FeedBackRequest;
import project.dto.response.FeedBackResponse;
import project.dto.response.PaginationFeedBack;
import project.dto.response.PaginationResponse;
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
import java.util.ArrayList;
import java.util.List;

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
        House house = houseRepository.findById(houseId).orElseThrow(() -> new NotFoundException("House not found"));
        Feedback feedback = new Feedback();
        feedback.setHouse(house);
        feedback.setUser(byEmail);
        feedback.setComment(feedBackRequest.getComm());
        feedback.setImages(feedBackRequest.getImages());

        feedback.setRating(feedBackRequest.getRating());

        feedBackRepository.save(feedback);

        double rating = houseRating(house.getFeedbacks());
        double roundedRating = Math.round(rating * 10.0) / 10.0;
        double limitedRating = Math.min(roundedRating, 5.0);
        house.setRating(limitedRating);
        houseRepository.save(house);

        return SimpleResponse.builder()
                .httpStatus(HttpStatus.OK)
                .message("Successfully saved feedback and updated house rating!")
                .build();
    }

    private double houseRating(List<Feedback> feedbacks) {
        if (feedbacks.isEmpty()) {
            return 0;
        }

        double sumRatings = 0;
        for (Feedback feedback : feedbacks) {
            sumRatings += feedback.getRating();
        }

        double averageRating = sumRatings / feedbacks.size();


        return averageRating * (5.0 / getMaxRating(feedbacks));
    }

    private double getMaxRating(List<Feedback> feedbacks) {
        double maxRating = Double.MIN_VALUE;
        for (Feedback feedback : feedbacks) {
            if (feedback.getRating() > maxRating) {
                maxRating = feedback.getRating();
            }
        }
        return maxRating;
    }

    @Override
    public SimpleResponse delete(Long feedId, Principal principal) {
        String name = principal.getName();
        User byEmail = userRepository.getByEmail(name);

        Feedback feedback = feedBackRepository.findById(feedId).orElseThrow(() -> new NotFoundException("feed not found"));
        if (feedback.getUser().getId().equals(byEmail.getId()) || byEmail.getRole().equals(Role.ADMIN)) {
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
        return new FeedBackResponse(feedback.getComment(), feedback.getRating(), feedback.getImages(),(long)feedback.getLikes().size(),(long)feedback.getDislikes().size());

    }

    @Override
    @Transactional
    public SimpleResponse updateFeed(FeedBackRequest feedBackRequest, Long feedId, Principal principal) {
        String name = principal.getName();
        User byEmail = userRepository.getByEmail(name);
        Feedback feedback = feedBackRepository.findById(feedId).orElseThrow(() -> new NotFoundException("feed not found"));

        if (feedback.getUser().getId().equals(byEmail.getId()) || byEmail.getRole().equals(Role.ADMIN)) {
            feedback.setComment(feedBackRequest.getComm());
            feedback.setRating(feedBackRequest.getRating());
            feedback.setImages(feedBackRequest.getImages());
            feedBackRepository.save(feedback);

            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("Success: Feedback updated")
                    .build();
        } else
            return SimpleResponse.builder()
                    .httpStatus(HttpStatus.OK)
                    .message("You cannot update this feedback")
                    .build();


    }

    @Override
    public PaginationFeedBack getAllFeedBack(Long houseId, int page, int size) {
        Pageable pageable = PageRequest.of(page - 1, size);
        Page<Feedback> all = feedBackRepository.findAllByHouseId(pageable, houseId);
        List<Feedback> content = all.getContent();
        log.info("SIZE! :" + content.size());
        List<FeedBackResponse> responses = new ArrayList<>();
        for (Feedback feedback : content) {
            FeedBackResponse convert = feedback.convert();
            convert.setLikes((long) feedback.getLikes().size());
            convert.setDislikes((long) feedback.getDislikes().size());
            responses.add(convert);
        }
        return PaginationFeedBack.builder().page(all.getNumber() + 1).
                size(all.getTotalPages()).feedBackResponses(responses).build();
    }

    @Override
    @Transactional
    public SimpleResponse like(Long feedBackId) {
        String emailCurrentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.getByEmail(emailCurrentUser);
        Feedback findFeedBack = feedBackRepository.findById(feedBackId).orElseThrow(() -> new NotFoundException("This feedback not found!  " + feedBackId));
        if (!findFeedBack.getLikes().contains(currentUser.getId())) {
            findFeedBack.getLikes().add(currentUser.getId());
            findFeedBack.getDislikes().remove(currentUser.getId());
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success liked this feedback!  " + feedBackId).build();
        }
        findFeedBack.getLikes().remove(currentUser.getId());
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success Un liked this feedback!  " + feedBackId).build();
    }

    @Override
    @Transactional
    public SimpleResponse disLike(Long feedBackId) {
        String emailCurrentUser = SecurityContextHolder.getContext().getAuthentication().getName();
        User currentUser = userRepository.getByEmail(emailCurrentUser);
        Feedback findFeedBack = feedBackRepository.findById(feedBackId).orElseThrow(() -> new NotFoundException("This feedback not found!  " + feedBackId));
        if (!findFeedBack.getDislikes().contains(currentUser.getId())) {
            findFeedBack.getDislikes().add(currentUser.getId());
            findFeedBack.getLikes().remove(currentUser.getId());
            return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success dislike this feed back!   " + feedBackId).build();
        }
        findFeedBack.getDislikes().remove(currentUser.getId());
        return SimpleResponse.builder().httpStatus(HttpStatus.OK).message("Success delete disLike: " + feedBackId).build();
    }
}