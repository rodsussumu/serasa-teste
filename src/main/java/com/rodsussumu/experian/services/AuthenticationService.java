package com.rodsussumu.experian.services;

import com.rodsussumu.experian.config.TokenService;
import com.rodsussumu.experian.dtos.LoginRequestDTO;
import com.rodsussumu.experian.dtos.LoginResponseDTO;
import com.rodsussumu.experian.exceptions.InternalServerErrorException;
import com.rodsussumu.experian.exceptions.ValidateException;
import com.rodsussumu.experian.models.User;
import com.rodsussumu.experian.repositories.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    AuthenticationService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager authenticationManager, TokenService tokenService) {
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    public ResponseEntity<LoginResponseDTO> login(LoginRequestDTO loginRequestDTO) {
        try {
            UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(loginRequestDTO.username(), loginRequestDTO.password());

            Authentication authentication = authenticationManager.authenticate(usernamePassword);

            UserDetails user = userRepository.findByUsername(loginRequestDTO.username());

            if(Objects.isNull(user)) {
                throw new ValidateException("Username or password is invalid!");
            }

            String token = tokenService.generateToken((User) authentication.getPrincipal());

            LoginResponseDTO loginResponseDTO = LoginResponseDTO.builder().token(token).username(loginRequestDTO.username()).build();

            return ResponseEntity.ok(loginResponseDTO);
        } catch (BadCredentialsException ex) {
            throw new ValidateException("Username or password is invalid!");
        } catch (Exception ex) {
            throw new InternalServerErrorException("Internal Server Error");
        }
    }
}
