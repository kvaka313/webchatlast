package ua.unicyb.webchat.exceptions;

public class KafkaSendException extends RuntimeException {

    public KafkaSendException(String message, Throwable e) {
        super(message, e);
    }
}
