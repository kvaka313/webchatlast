package ua.unicyb.webchat.message;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@AllArgsConstructor
public class MessageProvider {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendUserToAuthProvider(String message) {
        kafkaTemplate.send("keycloak-topic", message)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("Message sent to topic: {}", message);
                    } else {
                        log.error("Failed to send message", ex);
                    }
                });
    }
}
