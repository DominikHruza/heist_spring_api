package com.hackatonapi.HackatonRest.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.ZoneId;
import java.time.ZonedDateTime;


@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

    @org.springframework.web.bind.annotation.ExceptionHandler(value = ResourceNotFoundException.class)
    public ResponseEntity<Object> handleResourceNotFound(
            ResourceNotFoundException exception) {
        HttpStatus notFound = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = new ErrorResponse(
                notFound,
                exception.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(errorResponse, notFound);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = DuplicateResourceEntryException.class)
    public ResponseEntity<Object> handleResourceNotFound(
            DuplicateResourceEntryException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse(
                badRequest,
                exception.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(errorResponse, badRequest);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = NotAMemberSkillException.class)
    public ResponseEntity<Object> handleResourceNotFound(
            NotAMemberSkillException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse(
                badRequest,
                exception.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(errorResponse, badRequest);
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(value = HeistTimestampException.class)
    public ResponseEntity<Object> handleResourceNotFound(
            HeistTimestampException exception) {
        HttpStatus badRequest = HttpStatus.BAD_REQUEST;
        ErrorResponse errorResponse = new ErrorResponse(
                badRequest,
                exception.getMessage(),
                ZonedDateTime.now(ZoneId.of("Z"))
        );
        return new ResponseEntity<>(errorResponse, badRequest);
    }
}
