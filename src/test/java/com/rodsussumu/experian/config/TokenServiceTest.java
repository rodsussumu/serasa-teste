package com.rodsussumu.experian.config;

import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.rodsussumu.experian.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class TokenServiceTest {

    @InjectMocks
    private TokenService tokenService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(tokenService, "secretKey", "secret-token");
    }

    @Test
    void shouldGenerate() {
        User user = User.builder()
                .username("testUser")
                .build();

        String token = tokenService.generateToken(user);

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }

    @Test
    void shouldValidateToken() {
        User user = User.builder()
                .username("testUser")
                .build();
        String token = tokenService.generateToken(user);

        String username = tokenService.validateToken(token);

        assertEquals("testUser", username);
    }

    @Test
    void shouldReturnEmptyStringWhenTokenIsInvalid() {
        String invalidToken = "invalid.token";

        String username = tokenService.validateToken(invalidToken);

        assertTrue(username.isEmpty());
    }

    @Test
    void shouldThrowRuntimeExceptionWhenTokenGenerationFails() {
        User user = User.builder()
                .username("testUser")
                .build();

        try (MockedStatic<Algorithm> algorithmMock = Mockito.mockStatic(Algorithm.class)) {
            algorithmMock.when(() -> Algorithm.HMAC256("secret-token")).thenThrow(JWTCreationException.class);

            RuntimeException exception = assertThrows(RuntimeException.class, () -> {
                tokenService.generateToken(user);
            });

            assertEquals("Error Generating Token", exception.getMessage());
        }
    }
}