package com.rodsussumu.experian.config;

import com.rodsussumu.experian.indicator.RoleIndicator;
import com.rodsussumu.experian.models.Role;
import com.rodsussumu.experian.models.User;
import com.rodsussumu.experian.repositories.RoleRepository;
import com.rodsussumu.experian.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Objects;
import java.util.Optional;
import java.util.Set;

@Configuration
public class AdminConfig implements CommandLineRunner {
    private static final String ADMIN_USER = "admin";
    private static final String BASIC_USER = "user";


    private RoleRepository roleRepository;
    private UserRepository userRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AdminConfig(RoleRepository roleRepository, UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public void run(String... args) throws Exception {
        Role role = roleRepository.findByName(RoleIndicator.ADMIN.toString());

        UserDetails userAdmin = userRepository.findByUsername(ADMIN_USER);

        if(Objects.isNull(userAdmin)) {
            User user = User.builder()
                    .username(ADMIN_USER)
                    .password(bCryptPasswordEncoder.encode("1234"))
                    .roles(Set.of(role))
                    .build();
            userRepository.save(user);
            System.out.println("Admin criado com sucesso!");
        } else {
            System.out.println("Admin já cadastrado");
        }

        Role roleUser = roleRepository.findByName(RoleIndicator.USER.toString());

        UserDetails basicUser = userRepository.findByUsername(BASIC_USER);

        if(Objects.isNull(basicUser)) {
            User user = User.builder()
                    .username(BASIC_USER)
                    .password(bCryptPasswordEncoder.encode("1234"))
                    .roles(Set.of(roleUser))
                    .build();
            userRepository.save(user);
            System.out.println("Usuário básico criado com sucesso!");
        } else {
            System.out.println("Usuário básico já cadastrado");
        }
    }
}
