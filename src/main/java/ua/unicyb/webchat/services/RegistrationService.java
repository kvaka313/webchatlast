package ua.unicyb.webchat.services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ua.unicyb.webchat.convertors.UserConvertor;
import ua.unicyb.webchat.dto.RegistrationDto;
import ua.unicyb.webchat.dto.RegistrationResponseDto;
import ua.unicyb.webchat.entity.User;
import ua.unicyb.webchat.repositories.UserRepository;

@Service
@AllArgsConstructor
public class RegistrationService {

    private UserRepository userRepository;

    private UserConvertor userConvertor;

    @Transactional
    public RegistrationResponseDto registration(RegistrationDto registrationDto) {

        User user = userConvertor.covertToUserFromRegistration(registrationDto);

        User userFromLogin = userRepository.findByLogin(user.getLogin());

        if(userFromLogin != null) {
            return new RegistrationResponseDto(user.getLogin(),
                    String.format("User already exist with login %s", user.getLogin()));
        }

        User savedUser = userRepository.save(user);

        return userConvertor.convertFromUserToRegistrationResponse(savedUser);
    }
}
