package ua.unicyb.webchat.services;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import ua.unicyb.webchat.entity.Message;
import ua.unicyb.webchat.entity.User;
import ua.unicyb.webchat.repositories.MessageRepository;
import ua.unicyb.webchat.repositories.UserRepository;

import java.util.List;

@Service
public class PrivateMessageService {
    private MessageRepository messageRepository;

    private UserRepository userRepository;

    public PrivateMessageService(MessageRepository messageRepository,
                                 UserRepository userRepository){
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void saveMessageToDatabase(String sender, String receiver, String message){
        User recv = userRepository.findByLogin(receiver);
        User send = userRepository.findByLogin(sender);
        Message mesg = new Message();
        mesg.setMessage(message);
        if(send != null) {
            mesg.setSender(send);
        }
        if(recv != null){
            mesg.setReceiver(recv);
        }
        messageRepository.save(mesg);
    }

    public List<Message> getAll(String login){
        return messageRepository.findByReceiver_Login(login);
    }
}
