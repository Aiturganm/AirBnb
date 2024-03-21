package project.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import project.entities.Address;
import project.entities.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Long> {
}
