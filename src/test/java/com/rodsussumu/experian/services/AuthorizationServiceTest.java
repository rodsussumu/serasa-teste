package com.rodsussumu.experian.services;

import com.rodsussumu.experian.models.User;
import com.rodsussumu.experian.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;


class AuthorizationServiceTest {

    @Mock
    private UserRepository userRepository;

    @BeforeEach
    void setUp() {
        // Inicializa os mocks
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername() {
        String usernameMock = "testUser";
        User mockUser = new User();
        mockUser.setUsername(usernameMock);
        mockUser.setPassword("password");

        Mockito.when(userRepository.findByUsername(usernameMock)).thenReturn(mockUser);

        UserDetails user = userRepository.findByUsername(usernameMock);

        Assertions.assertEquals(user, mockUser);
    }
}