package com.rodsussumu.experian.services;

import com.rodsussumu.experian.models.User;
import com.rodsussumu.experian.repositories.UserRepository;
import jakarta.inject.Inject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;


class AuthorizationServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthorizationService authorizationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldGetUserDetails() {
        String usernameMock = "testUser";
        User mockUser = new User();
        mockUser.setUsername(usernameMock);
        mockUser.setPassword("password");

        Mockito.when(userRepository.findByUsername(usernameMock)).thenReturn(mockUser);

        UserDetails user = authorizationService.loadUserByUsername(usernameMock);

        Assertions.assertEquals(user, mockUser);
    }
}