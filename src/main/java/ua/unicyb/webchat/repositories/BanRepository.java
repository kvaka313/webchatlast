package ua.unicyb.webchat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.unicyb.webchat.entity.Ban;

public interface BanRepository extends JpaRepository<Ban, Long> {
}
