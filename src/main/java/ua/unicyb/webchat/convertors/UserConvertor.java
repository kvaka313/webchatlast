package ua.unicyb.webchat.convertors;

import org.mapstruct.Mapper;
import ua.unicyb.webchat.dto.RegistrationDto;
import ua.unicyb.webchat.dto.RegistrationResponseDto;
import ua.unicyb.webchat.entity.User;

@Mapper(componentModel = "spring")
public interface UserConvertor {

    User covertToUserFromRegistration(RegistrationDto registrationDto);

    RegistrationResponseDto convertFromUserToRegistrationResponse(User user);

}
