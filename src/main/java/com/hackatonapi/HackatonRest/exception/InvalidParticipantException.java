package com.hackatonapi.HackatonRest.exception;

public class InvalidParticipantException extends RuntimeException {

    public InvalidParticipantException() {
    }

    public InvalidParticipantException(String message) {
        super(message);
    }

    public InvalidParticipantException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidParticipantException(Throwable cause) {
        super(cause);
    }

    public InvalidParticipantException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
