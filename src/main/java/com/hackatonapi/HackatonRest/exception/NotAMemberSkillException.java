package com.hackatonapi.HackatonRest.exception;

public class NotAMemberSkillException extends RuntimeException  {
    public NotAMemberSkillException() {
    }

    public NotAMemberSkillException(String message) {
        super(message);
    }

    public NotAMemberSkillException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotAMemberSkillException(Throwable cause) {
        super(cause);
    }

    public NotAMemberSkillException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
