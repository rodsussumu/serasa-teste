package com.rodsussumu.experian.services;

import com.rodsussumu.experian.config.TokenService;
import com.rodsussumu.experian.dtos.LoginRequestDTO;
import com.rodsussumu.experian.dtos.LoginResponseDTO;
import com.rodsussumu.experian.models.Role;
import com.rodsussumu.experian.models.User;
import com.rodsussumu.experian.repositories.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collections;
import java.util.Set;

import static org.mockito.Mockito.mock;

class AuthenticationServiceTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private TokenService tokenService;

    @InjectMocks
    private AuthenticationService authenticationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnAuthenticationSuccessfully() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("testUser", "password");

        Role userRole = new Role();
        userRole.setName("USER");
        Set<Role> roles = Set.of(userRole);

        UserDetails mockUser = User.builder()
                .username("testUser")
                .password("password")
                .roles(Collections.singleton(userRole))
                .build();

        Authentication mockAuthentication = mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        Mockito.when(mockAuthentication.getPrincipal()).thenReturn(mockUser);
        Mockito.when(userRepository.findByUsername("testUser")).thenReturn(mockUser);
        Mockito.when(tokenService.generateToken((User) mockUser)).thenReturn("mockToken");

        ResponseEntity<LoginResponseDTO> response = authenticationService.login(loginRequestDTO);

        Assertions.assertNotNull(response);
        Assertions.assertEquals("mockToken", response.getBody().token());
        Assertions.assertEquals("testUser", response.getBody().username());
    }

    @Test
    void shouldInvalidLoginWhenInvalidCredentials() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("invalidUser", "invalidPassword");

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        BadCredentialsException exception = Assertions.assertThrows(BadCredentialsException.class, () -> {
            authenticationService.login(loginRequestDTO);
        });

        Assertions.assertEquals("Username or password is invalid!", exception.getMessage());
    }

    @Test
    void shouldInvalidLoginWhenUsernameNotFound() {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("unknownUser", "password");

        Authentication mockAuthentication = mock(Authentication.class);

        Mockito.when(authenticationManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(mockAuthentication);
        Mockito.when(userRepository.findByUsername("unknownUser")).thenReturn(null);

        BadCredentialsException exception = Assertions.assertThrows(BadCredentialsException.class, () -> {
            authenticationService.login(loginRequestDTO);
        });

        Assertions.assertEquals("Username or password is invalid!", exception.getMessage());
    }
}