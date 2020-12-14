package com.hackatonapi.HackatonRest.exception;

public class HeistTimestampException extends RuntimeException {

    public HeistTimestampException() {
    }

    public HeistTimestampException(String message) {
        super(message);
    }

    public HeistTimestampException(String message, Throwable cause) {
        super(message, cause);
    }

    public HeistTimestampException(Throwable cause) {
        super(cause);
    }

    public HeistTimestampException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
