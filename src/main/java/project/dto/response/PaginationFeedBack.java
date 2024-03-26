package project.dto.response;

import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationFeedBack {
    private int page;
    private int size;
    private List<FeedBackResponse> feedBackResponses = new ArrayList<>();
}
