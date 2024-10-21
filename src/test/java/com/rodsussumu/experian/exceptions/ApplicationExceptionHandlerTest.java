package com.rodsussumu.experian.exceptions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationExceptionHandlerTest {

    private ApplicationExceptionHandler exceptionHandler;

    @BeforeEach
    void setUp() {
        exceptionHandler = new ApplicationExceptionHandler();
    }

    @Test
    void shouldHandleBadCredentialsException() {
        BadCredentialsException exception = new BadCredentialsException("Invalid credentials");

        ResponseEntity<?> response = exceptionHandler.handleBadCredentialsException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ExceptionDetails details = (ExceptionDetails) response.getBody();
        assertNotNull(details);
        assertEquals("Invalid credentials", details.getError());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), details.getCode());
    }

    @Test
    void shouldHandleRuntimeException() {
        RuntimeException exception = new RuntimeException("Runtime error");

        ResponseEntity<?> response = exceptionHandler.handleRunTimeException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        ExceptionDetails details = (ExceptionDetails) response.getBody();
        assertNotNull(details);
        assertEquals("Runtime error", details.getError());
        assertEquals(HttpStatus.UNAUTHORIZED.value(), details.getCode());
    }

    @Test
    void shouldHandleDuplicateKeyException() {
        DuplicateKeyException exception = new DuplicateKeyException("Duplicate key error");

        ResponseEntity<?> response = exceptionHandler.handleDuplicateKey(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ExceptionDetails details = (ExceptionDetails) response.getBody();
        assertNotNull(details);
        assertEquals("Duplicate key error", details.getError());
        assertEquals(HttpStatus.BAD_REQUEST.value(), details.getCode());
    }

    @Test
    void shouldHandleIllegalArgumentException() {
        IllegalArgumentException exception = new IllegalArgumentException("Invalid argument");

        ResponseEntity<?> response = exceptionHandler.handleIllegalArgumentException(exception);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        ExceptionDetails details = (ExceptionDetails) response.getBody();
        assertNotNull(details);
        assertEquals("Invalid argument", details.getError());
        assertEquals(HttpStatus.BAD_REQUEST.value(), details.getCode());
    }
}