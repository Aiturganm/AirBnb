package project.service.serviceImpl;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import project.enums.Role;

@Getter
@Setter
@Builder
public class UserResponse {
    String token;
    String email;
    String message;
    Role role;
    HttpStatus httpStatus;
}