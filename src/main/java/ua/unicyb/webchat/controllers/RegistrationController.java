package ua.unicyb.webchat.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ua.unicyb.webchat.dto.RegistrationDto;
import ua.unicyb.webchat.dto.RegistrationResponseDto;
import ua.unicyb.webchat.exceptions.KafkaSendException;
import ua.unicyb.webchat.message.MessageProvider;
import ua.unicyb.webchat.services.UserService;

@RestController
@AllArgsConstructor
public class RegistrationController {

    private UserService userService;

    private MessageProvider messageProvider;

    private ObjectMapper mapper;

    @PostMapping("/registration")
    public RegistrationResponseDto registration(@Valid @RequestBody RegistrationDto registrationDto) {
        RegistrationResponseDto registrationResponseDto =  userService.registration(registrationDto);
        try {
            if (registrationResponseDto.getErrorMessage() == null) {
                messageProvider.sendUserToAuthProvider(mapper.writeValueAsString(registrationDto));
            }
            return registrationResponseDto;
        } catch(JsonProcessingException e) {
            throw new KafkaSendException("Can not send message to keycloak", e);
        }
    }
}
