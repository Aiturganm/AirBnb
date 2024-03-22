package project.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@MappedSuperclass
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,generator = "id_gen")
    private Long id;
    private LocalDate createdAt;
    private LocalDate updatedAt;
    @PrePersist
    private void prePersist(){
        this.createdAt = LocalDate.now();
    }
    @PreUpdate
    private void preUpdate(){
        this.updatedAt = LocalDate.now();
    }
}
