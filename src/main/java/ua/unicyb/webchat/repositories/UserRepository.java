package ua.unicyb.webchat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.unicyb.webchat.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByLogin(String login); //select * from chat_users where login =

}
