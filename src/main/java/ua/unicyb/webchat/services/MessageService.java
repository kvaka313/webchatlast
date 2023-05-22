package ua.unicyb.webchat.services;


import org.springframework.stereotype.Service;
import ua.unicyb.webchat.convertors.MessageConverter;
import ua.unicyb.webchat.dto.SendMessage;
import ua.unicyb.webchat.entity.BroadcastMessage;
import ua.unicyb.webchat.entity.Message;

import java.util.List;

@Service
public class MessageService {
    private PrivateMessageService privateMessageService;

    private BroadcastService broadcastService;

    private MessageConverter messageConverter;

    public MessageService(PrivateMessageService messageService,
                            BroadcastService broadcastService,
                            MessageConverter messageConverter){
        this.privateMessageService = messageService;
        this.broadcastService = broadcastService;
        this.messageConverter = messageConverter;
    }

    public void saveMessage(String sender, String receiver, String message){
        privateMessageService.saveMessageToDatabase(sender, receiver, message);
    }

    public void saveBroadCastMessage(String sender, String messageToSend){
        broadcastService.saveBroadcast(sender, messageToSend);
    }

    public List<SendMessage> getAllMessages(String login){
        List<Message> privateMessages = privateMessageService.getAll(login);
        List<SendMessage> privateMessageDtos = messageConverter
                .toListPrivateDto(privateMessages);
        List<BroadcastMessage> broadcastMessages = broadcastService.getAll();
        List<SendMessage> broadcastMessageDtos = null;
        if(broadcastMessages != null) {
            broadcastMessageDtos = messageConverter
                    .toListBroadcastDtos(broadcastMessages);
        }
        if(privateMessageDtos != null && broadcastMessageDtos != null){
            privateMessageDtos.addAll(broadcastMessageDtos);
            return privateMessageDtos;
        }
        if(privateMessageDtos != null && broadcastMessageDtos == null){
            return privateMessageDtos;
        }
        if(privateMessageDtos == null && broadcastMessageDtos != null) {
            return broadcastMessageDtos;
        }
        return null;
    }
}
