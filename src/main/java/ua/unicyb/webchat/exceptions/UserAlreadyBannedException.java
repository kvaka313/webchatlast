package ua.unicyb.webchat.exceptions;

public class UserAlreadyBannedException extends RuntimeException{
    public UserAlreadyBannedException(String message) {
        super(message);
    }
}
