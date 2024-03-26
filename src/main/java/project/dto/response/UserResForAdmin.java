package project.dto.response;

import lombok.*;
import project.enums.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public class UserResForAdmin {
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dateOfBirth;
    private Role role;
    private boolean isBlock;
    private String phoneNumber;
    private List<HouseResForAdmin> houseResForAdminList = new ArrayList<>();

    public UserResForAdmin(String firstName, String lastName, String email, LocalDate dateOfBirth, Role role, boolean isBlock, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.isBlock = isBlock;
        this.phoneNumber = phoneNumber;
    }
}
