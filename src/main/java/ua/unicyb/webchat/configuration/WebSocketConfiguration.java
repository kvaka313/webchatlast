package ua.unicyb.webchat.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import ua.unicyb.webchat.controllers.WebSocketController;

@Configuration
@EnableWebSocket
public class WebSocketConfiguration  implements WebSocketConfigurer {

    @Autowired
    private ApplicationContext context;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(getWebSocketController(), "/socket")
                .setAllowedOrigins("*")
                .addInterceptors(new SecurityInterceptor(serviceName))
                .withSockJS();

    }

    private WebSocketController getWebSocketController(){
        return new WebSocketController(context);
    }
}
