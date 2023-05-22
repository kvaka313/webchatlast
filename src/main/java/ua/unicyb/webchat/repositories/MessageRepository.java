package ua.unicyb.webchat.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ua.unicyb.webchat.entity.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByReceiver_Login(String login);
    // select * from messages msg inner join users usr ON msg.receiver_id = usr.id where usr.login = 'login'

}
