package ua.unicyb.webchat.exceptions;

import ua.unicyb.webchat.exceptions.UserNotFoundException;

public class UserNotBannedException extends RuntimeException {
    public UserNotBannedException(String message) {
        super(message);
    }
}
