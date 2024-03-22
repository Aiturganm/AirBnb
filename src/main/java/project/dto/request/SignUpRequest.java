package project.dto.request;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import project.enums.Role;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class SignUpRequest{
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String email;
    private String password;
    private String phoneNumber;
    private Role role;
    private int experience;
}
