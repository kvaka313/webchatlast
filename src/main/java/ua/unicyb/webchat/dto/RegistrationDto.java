package ua.unicyb.webchat.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class RegistrationDto {

    @NotBlank(message = "Field name can not be empty")
    private String name;
    @NotBlank(message = "Field login can not be empty")
    private String login;
    @NotBlank(message = "Field password can not be empty")
    private String password;
}
