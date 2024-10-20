package com.rodsussumu.experian.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(ValidateException.class)
    public ResponseEntity<?> validateException(ValidateException ex) {
        ExceptionDetails details = new ExceptionDetails(ex.getMessage(), HttpStatus.UNAUTHORIZED.value());
        return new ResponseEntity<>(details, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InternalServerErrorException.class)
    public ResponseEntity<?> internalServerException(ValidateException ex) {
        ExceptionDetails details = new ExceptionDetails(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> runTimeException(ValidateException ex) {
        ExceptionDetails details = new ExceptionDetails(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR.value());
        return new ResponseEntity<>(details, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
