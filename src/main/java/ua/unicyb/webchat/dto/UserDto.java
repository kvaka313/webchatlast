package ua.unicyb.webchat.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private String login;
    private String name;
    private Boolean isBanned;
}
