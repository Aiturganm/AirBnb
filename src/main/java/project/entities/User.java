package project.entities;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import project.enums.Role;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users")
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SequenceGenerator(name = "user_gen",allocationSize = 1)
public class User extends BaseEntity implements UserDetails {
        private String firstName;
        private String lastName;
        private String email;
        private String password;
        private LocalDate dateOfBirth;
        @Enumerated(EnumType.STRING)
        private Role role;
        private boolean isBlock;
        private String phoneNumber;

    public User(Long id, LocalDate createdAt, LocalDate updatedAt, String firstName, String lastName, String email, String password, LocalDate dateOfBirth, Role role, String phoneNumber) {
        super(id, createdAt, updatedAt);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.phoneNumber = phoneNumber;
    }

    public User(String firstName, String lastName, String email, String password, LocalDate dateOfBirth, Role role, boolean isBlock, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.role = role;
        this.isBlock = isBlock;
        this.phoneNumber = phoneNumber;
    }

    //Relations
    @OneToOne
    private Card card;
    @OneToMany
    private List<House> houses = new ArrayList<>();


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
