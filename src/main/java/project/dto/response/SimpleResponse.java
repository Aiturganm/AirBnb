package project.dto.response;

import lombok.Builder;
import org.springframework.http.HttpStatus;
import lombok.Getter;
import lombok.Setter;
@Builder
@Getter
@Setter
public class SimpleResponse {
    HttpStatus httpStatus;
    String message;
}
