package project.entities;

import jakarta.persistence.*;
import lombok.*;
import project.enums.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@SequenceGenerator(name = "user_gen",allocationSize = 1)
public class User extends BaseEntity{
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDate dateOfBirth;
    @Enumerated(EnumType.STRING)
    private Role role;
    private boolean isBlock;
    @OneToMany
    private List<House> houses;
    @OneToMany
    private List<RentInfo> rentInfos;

}
