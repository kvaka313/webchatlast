package ua.unicyb.webchat.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
public class ReceiveMessage {
    @NotNull(message = "type is required")
    private String type;
    private String receiver;
    private String message;
}
