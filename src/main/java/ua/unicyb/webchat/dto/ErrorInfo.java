package ua.unicyb.webchat.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorInfo {
    private Long timestamp;
    private String message;
    private String developerMessage;
}
