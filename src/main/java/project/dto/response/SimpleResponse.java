package project.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;
<<<<<<< HEAD

@Builder
public class SimpleResponse {
    HttpStatus httpStatus;
    String message;
=======
@Builder
public record SimpleResponse(
        HttpStatus httpStatus,
        String message
) {
>>>>>>> origin/branchForUser
}
