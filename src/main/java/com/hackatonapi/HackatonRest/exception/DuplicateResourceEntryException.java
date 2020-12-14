package com.hackatonapi.HackatonRest.exception;


public class DuplicateResourceEntryException extends RuntimeException {

    public DuplicateResourceEntryException() {
    }

    public DuplicateResourceEntryException(String message) {
        super(message);
    }

    public DuplicateResourceEntryException(String message, Throwable cause) {
        super(message, cause);
    }

    public DuplicateResourceEntryException(Throwable cause) {
        super(cause);
    }

    public DuplicateResourceEntryException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}