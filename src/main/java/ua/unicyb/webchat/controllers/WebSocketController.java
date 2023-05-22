package ua.unicyb.webchat.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import ua.unicyb.webchat.dto.ReceiveMessage;
import ua.unicyb.webchat.dto.SendMessage;
import ua.unicyb.webchat.entity.User;
import ua.unicyb.webchat.services.MessageService;
import ua.unicyb.webchat.services.UserService;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class WebSocketController extends TextWebSocketHandler {

    public final static String LOGIN ="login";

    private UserService userService;

    private Map<String, WebSocketSession> activeUsers = new ConcurrentHashMap<>();

    private ObjectMapper mapper = new ObjectMapper();

    private Validator validator;

    private MessageService messageService;

    public WebSocketController(ApplicationContext applicationContext) {
        this.userService = applicationContext.getBean(UserService.class);
        this.validator = applicationContext.getBean(Validator.class);
        this.messageService = applicationContext.getBean(MessageService.class);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session){
        try {
            String login = (String) session.getAttributes().get(LOGIN);
            User user = userService.findUserByLogin(login);
            if (user != null) {
                if (user.getBan() != null) {
                    sendErrorMessage(session, "You are banned.");
                    return;
                }

                activeUsers.put(login, session);
                sendActiveUsersList();
                sendMessages(session);

            } else {
                log.warn("User does not exist with login {}", login);
                session.close();
            }
        } catch (IOException e){
            log.error(e.getMessage());
        }
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message){
        try {
            String login = (String) session.getAttributes().get("login");
            User user = userService.findUserByLogin(login);

            if (user != null && user.getBan() != null) {
                log.warn("User was benned with login {}", login);
                session.close();
            }

            String jsonString = message.getPayload();
            ReceiveMessage receiveMessage = mapper.readValue(jsonString, ReceiveMessage.class);

            Set<ConstraintViolation<ReceiveMessage>> errors = validator.validate(receiveMessage);
            if(!errors.isEmpty()){
                sendErrorMessage(session, errors.iterator().next().getMessage());
                return;
            }

            switch(receiveMessage.getType()){
                case "PRIVATE":{
                    if(receiveMessage.getReceiver() == null){
                        sendErrorMessage(session, "receiver is required.");
                        return;
                    }
                    if(userService.findUserByLogin(receiveMessage.getReceiver()) == null){
                        sendErrorMessage(session, "receiver was not found.");
                        return;
                    }

                    if(receiveMessage.getMessage() == null){
                        sendErrorMessage(session, "message is required");
                        return;
                    }
                    WebSocketSession receiverSession = activeUsers.get(receiveMessage.getReceiver());
                    if(receiverSession == null){
                        messageService.saveMessage(login,receiveMessage.getReceiver(), receiveMessage.getMessage());
                        return;
                    }

                    sendPrivateMessage(receiverSession, login, receiveMessage.getMessage());
                    break;
                }
                case "BROADCAST":{
                    if(receiveMessage.getMessage() == null){
                        sendErrorMessage(session, "message is required");
                        return;
                    }
                    sendBroadcastMessage(login, receiveMessage.getMessage());
                    break;
                }
                case "LOGOUT":{
                    activeUsers.remove(login);
                    sendActiveUsersList();
                    session.close();
                    break;
                }
            }

        } catch (IOException e){
            log.error(e.getMessage());
        }
    }

    private void sendErrorMessage(WebSocketSession session, String message){
        sendPrivateMessage(session, "system", message);
    }

    private void sendPrivateMessage(WebSocketSession receiveSession, String sender, String messageToSend){
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setType("PRIVATE");
            sendMessage.setSender(sender);
            sendMessage.setMessage(messageToSend);
            TextMessage textMessage = new TextMessage(mapper.writeValueAsString(sendMessage));
            receiveSession.sendMessage(textMessage);
        }catch(IOException e){
            log.error(e.getMessage());
        }
    }


    private void sendBroadcastMessage(String sender, String messageToSend){
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setType("BROADCAST");
            sendMessage.setSender(sender);
            sendMessage.setMessage(messageToSend);
            messageService.saveBroadCastMessage(sender, messageToSend);
            TextMessage textMessage = new TextMessage(mapper.writeValueAsString(sendMessage));
            sendAll(textMessage);
        }catch(IOException e){
            log.error(e.getMessage());
        }
    }

    private void sendAll(TextMessage textMessage){

        activeUsers.entrySet().stream()
                .map(entry->entry.getValue())
                .forEach(session -> {
                    try {
                        session.sendMessage(textMessage);
                    } catch (IOException e) {
                        log.error(e.getMessage());
                    }
                });
    }

    private void sendActiveUsersList(){
        try {
            Set<String> activeUsersLogin = activeUsers.keySet();
            SendMessage sendMessage = new SendMessage();
            sendMessage.setType("LIST");
            sendMessage.setSender("system");
            sendMessage.setMessage(null);
            sendMessage.setLogins(activeUsersLogin);
            TextMessage textMessage = new TextMessage(mapper.writeValueAsString(sendMessage));
            sendAll(textMessage);
        }catch (IOException e){
            log.error(e.getMessage());
        }
    }


    private void sendMessages(WebSocketSession session){
        try {
            List<SendMessage> messages = messageService.getAllMessages((String) session.getAttributes().get("login"));
            for(SendMessage sendMessage: messages){
                session.sendMessage(new TextMessage(mapper.writeValueAsString(sendMessage)));
            }
        }catch (IOException e){
            log.error(e.getMessage());
        }


    }



}
