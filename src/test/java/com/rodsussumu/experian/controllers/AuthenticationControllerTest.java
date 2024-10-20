package com.rodsussumu.experian.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rodsussumu.experian.config.TokenService;
import com.rodsussumu.experian.dtos.LoginRequestDTO;
import com.rodsussumu.experian.dtos.LoginResponseDTO;
import com.rodsussumu.experian.exceptions.ApplicationExceptionHandler;
import com.rodsussumu.experian.repositories.UserRepository;
import com.rodsussumu.experian.services.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@WebMvcTest(AuthenticationController.class)
@Import(ApplicationExceptionHandler.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthenticationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    private AuthenticationController authenticationController;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private AuthenticationService authenticationService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void shouldLoginSuccessfully() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("testUser", "password");
        LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder().username("testUser").token("mockToken").build();

        Mockito.when(authenticationService.login(Mockito.any(LoginRequestDTO.class))).thenReturn(ResponseEntity.ok(loginResponseDTO));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.username").value("testUser"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.token").value("mockToken"));
    }

    @Test
    void shouldInvalidLogin() throws Exception {
        LoginRequestDTO loginRequestDTO = new LoginRequestDTO("invalidUser", "invalidPassword");

        Mockito.when(authenticationService.login(Mockito.any(LoginRequestDTO.class))).thenThrow(new BadCredentialsException("Username or password is invalid!"));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginRequestDTO)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error").value("Username or password is invalid!"));
    }
}