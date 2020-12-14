package com.hackatonapi.HackatonRest.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
public class ErrorResponse {
    private HttpStatus status;
    private String message;
    private ZonedDateTime timestamp;
}
