package com.rodsussumu.experian.config;

import com.rodsussumu.experian.models.Role;
import com.rodsussumu.experian.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;


class SecurityFilterTest {

    @Mock
    private TokenService tokenService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private SecurityFilter securityFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        SecurityContextHolder.clearContext(); // Limpa o contexto antes de cada teste
    }


    @Test
    void shouldAuthenticationValidWhenValidToken() throws ServletException, IOException {
        String token = "secret-token";
        String username = "testUser";

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();

        Role role = new Role();
        role.setName("ADMIN");

        UserDetails userDetails = User.withUsername(username)
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        Mockito.when(tokenService.validateToken(token)).thenReturn(username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(userDetails);

        // Act
        securityFilter.doFilterInternal(request, response, filterChain);

        // Assert
        Assertions.assertNotNull(SecurityContextHolder.getContext().getAuthentication());
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenTokenInvalid() throws ServletException, IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer invalid-token");
        MockHttpServletResponse response = new MockHttpServletResponse();

        Mockito.when(tokenService.validateToken("invalid-token")).thenReturn(null);

        securityFilter.doFilterInternal(request, response, filterChain);

        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenAuthorizationEmpty() throws ServletException, IOException {
        // Arrange
        MockHttpServletRequest request = new MockHttpServletRequest();
        MockHttpServletResponse response = new MockHttpServletResponse();

        // Act
        securityFilter.doFilterInternal(request, response, filterChain);

        // Assert
        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }

    @Test
    void shouldNotAuthenticateWhenUserNull() throws ServletException, IOException {
        String token = "valid-token";
        String username = "nonExistentUser";
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader("Authorization", "Bearer " + token);
        MockHttpServletResponse response = new MockHttpServletResponse();

        Mockito.when(tokenService.validateToken(token)).thenReturn(username);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(null);

        securityFilter.doFilterInternal(request, response, filterChain);

        Assertions.assertNull(SecurityContextHolder.getContext().getAuthentication());
        Mockito.verify(filterChain, Mockito.times(1)).doFilter(request, response);
    }
}