package project.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;
@Builder
public class SimpleResponse {
    HttpStatus httpStatus;
    String message;
}
