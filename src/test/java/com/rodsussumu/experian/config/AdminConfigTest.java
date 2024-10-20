package com.rodsussumu.experian.config;

import com.rodsussumu.experian.indicator.RoleIndicator;
import com.rodsussumu.experian.models.Role;
import com.rodsussumu.experian.models.User;
import com.rodsussumu.experian.repositories.RoleRepository;
import com.rodsussumu.experian.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

class AdminConfigTest {
    @Mock
    private RoleRepository roleRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private AdminConfig adminConfig;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateAdminIfAdminNotRegistered() throws Exception {
        // Arrange
        Role adminRole = new Role();
        adminRole.setName(RoleIndicator.ADMIN.name());

        Mockito.when(roleRepository.findByName(RoleIndicator.ADMIN.name())).thenReturn(adminRole);
        Mockito.when(userRepository.findByUsername("admin")).thenReturn(null);
        Mockito.when(bCryptPasswordEncoder.encode("1234")).thenReturn("encoded-password");

        // Act
        adminConfig.run();

        // Assert
        Mockito.verify(userRepository, Mockito.times(1)).save(Mockito.any(User.class));
    }

    @Test
    void shouldNotCreateAdminIfAdminRegistered() throws Exception {
        // Arrange
        Role adminRole = new Role();
        adminRole.setName(RoleIndicator.ADMIN.name());

        UserDetails existingAdmin = org.springframework.security.core.userdetails.User
                .withUsername("admin")
                .password("encoded-password")
                .authorities(Collections.emptyList())
                .build();

        Mockito.when(roleRepository.findByName(RoleIndicator.ADMIN.name())).thenReturn(adminRole);
        Mockito.when(userRepository.findByUsername("admin")).thenReturn(existingAdmin);

        // Act
        adminConfig.run();

        // Assert
        Mockito.verify(userRepository, Mockito.never()).save(Mockito.any(User.class));
    }

}