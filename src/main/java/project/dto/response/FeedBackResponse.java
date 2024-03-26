package project.dto.response;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class FeedBackResponse {
    String comm;
    private double rating;
    List<String> images;
    Long likes;
    Long dislikes;

    public FeedBackResponse(String comm, double rating) {
        this.comm = comm;
        this.rating = rating;
    }

    public FeedBackResponse(String comm, double rating, List<String> images) {
        this.comm = comm;
        this.rating = rating;
        this.images = images;
    }
}
