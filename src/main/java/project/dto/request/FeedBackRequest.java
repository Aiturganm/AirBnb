package project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import project.entities.Card;
import project.entities.Feedback;
import project.validation.RatingValidation;

import java.util.List;

@Getter
@Setter
public class FeedBackRequest {
    @NotBlank
    String comm;
    @NotNull
    private List<String> images;
    @RatingValidation
    private double rating;

}
