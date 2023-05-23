package ua.unicyb.webchat.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import ua.unicyb.webchat.dto.ErrorInfo;
import ua.unicyb.webchat.exceptions.UserAlreadyBannedException;
import ua.unicyb.webchat.exceptions.UserNotBannedException;
import ua.unicyb.webchat.exceptions.UserNotFoundException;

@ControllerAdvice
public class ExceptionHandlerService {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({
            UserAlreadyBannedException.class,
            UserNotFoundException.class,
            UserNotBannedException.class})
    @ResponseBody
    public ErrorInfo exceptionHandler(Exception ex){
        return ErrorInfo.builder().timestamp(System.currentTimeMillis())
                .message(ex.getMessage())
                .developerMessage(ex.toString())
                .build();
    }
}
