package project.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.validator.internal.util.privilegedactions.LoadClass;

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
    private LocalDate createdAd;
    private LocalDate updateAd;
    @PrePersist
    private void prePersist(){
        this.createdAd = LocalDate.now();
    }
    @PreUpdate
    private void preUpdate(){
        this.updateAd = LocalDate.now();
    }
}
