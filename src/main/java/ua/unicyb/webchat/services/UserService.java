package ua.unicyb.webchat.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ua.unicyb.webchat.convertors.UserConvertor;
import ua.unicyb.webchat.dto.RegistrationDto;
import ua.unicyb.webchat.dto.RegistrationResponseDto;
import ua.unicyb.webchat.dto.UserDto;
import ua.unicyb.webchat.entity.Ban;
import ua.unicyb.webchat.entity.User;
import ua.unicyb.webchat.exceptions.UserAlreadyBannedException;
import ua.unicyb.webchat.exceptions.UserNotFoundException;
import ua.unicyb.webchat.repositories.BanRepository;
import ua.unicyb.webchat.exceptions.UserNotBannedException;
import ua.unicyb.webchat.repositories.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class UserService {

    private UserRepository userRepository;

    private BanRepository banRepository;

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

    public User findUserByLogin(String login) {
        return userRepository.findByLogin(login);
    }

    @Transactional(readOnly = true)
    public List<UserDto> findAll() {
        List<User> users = userRepository.findAll();
        return userConvertor.convertUserList(users);
    }

    public UserDto banUser(String login) {
        User userFromLogin = userRepository.findByLogin(login);

        if(userFromLogin == null) {
            throw new UserNotFoundException(String.format("User does not find with login %s", login));
        }

        if(userFromLogin.getBan() != null) {
            throw new UserAlreadyBannedException(String.format("User is banned with login %s", login));
        }

        Ban ban =  new Ban();
        ban.setUser(userFromLogin);
        banRepository.save(ban);
        userFromLogin.setBan(ban);
        userRepository.save(userFromLogin);
        return userConvertor.convertUser(userFromLogin);
    }

    public UserDto unbanUser(String login) {
        User userFromLogin = userRepository.findByLogin(login);
        if(userFromLogin == null) {
            throw new UserNotFoundException(String.format("User does not find with login %s", login));
        }

        if(userFromLogin.getBan() == null) {
            throw new UserNotBannedException(String.format("User is banned with login %s", login));
        }

        Ban ban = userFromLogin.getBan();

        userFromLogin.setBan(null);
        banRepository.delete(ban);
        userRepository.save(userFromLogin);

        return userConvertor.convertUser(userFromLogin);

    }
}
