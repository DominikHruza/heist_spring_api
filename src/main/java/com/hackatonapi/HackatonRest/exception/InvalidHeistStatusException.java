package com.hackatonapi.HackatonRest.exception;

public class InvalidHeistStatusException extends RuntimeException {
    public InvalidHeistStatusException() {
    }

    public InvalidHeistStatusException(String message) {
        super(message);
    }

    public InvalidHeistStatusException(String message, Throwable cause) {
        super(message, cause);
    }

    public InvalidHeistStatusException(Throwable cause) {
        super(cause);
    }

    public InvalidHeistStatusException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
