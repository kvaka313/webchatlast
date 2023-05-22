package ua.unicyb.webchat.services;

import org.springframework.data.redis.core.RedisTemplate;
import ua.unicyb.webchat.entity.BroadcastMessage;

import java.util.List;

public class BroadcastService {

    private RedisTemplate<String, BroadcastMessage> redisTemplate;

    public BroadcastService(RedisTemplate<String, BroadcastMessage> redisTemplate){
        this.redisTemplate = redisTemplate;
    }


    public void saveBroadcast(String sender, String messageToSend){
        BroadcastMessage broadcastMessage = new BroadcastMessage();
        broadcastMessage.setSender(sender);
        broadcastMessage.setMessage(messageToSend);
        redisTemplate.opsForList().leftPush("broadcast", broadcastMessage);
    }

    public List<BroadcastMessage> getAll(){
        try {
            return redisTemplate.opsForList().range("broadcast", 0, -1);
        } catch(Exception e){
            return null;
        }
    }
}