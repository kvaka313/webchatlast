package ua.unicyb.webchat.convertors;

import org.mapstruct.Mapper;
import ua.unicyb.webchat.dto.RegistrationDto;
import ua.unicyb.webchat.dto.RegistrationResponseDto;
import ua.unicyb.webchat.dto.UserDto;
import ua.unicyb.webchat.entity.User;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserConvertor {

    User covertToUserFromRegistration(RegistrationDto registrationDto);

    RegistrationResponseDto convertFromUserToRegistrationResponse(User user);

    List<UserDto> convertUserList(List<User> users);

    default UserDto convertUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setLogin(user.getLogin());
        if(user.getBan() != null) {
            userDto.setIsBanned(true);
        } else {
            userDto.setIsBanned(false);
        }
        return userDto;
    }

}
