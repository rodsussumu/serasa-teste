package com.rodsussumu.experian.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleBadCredentialsException(BadCredentialsException ex) {
        ExceptionDetails details = new ExceptionDetails(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(details, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> handleRunTimeException(RuntimeException ex) {
        ExceptionDetails details = new ExceptionDetails(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(details, HttpStatus.UNAUTHORIZED);
    }

}
