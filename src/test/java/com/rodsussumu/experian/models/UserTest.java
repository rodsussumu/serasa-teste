package com.rodsussumu.experian.models;

import com.rodsussumu.experian.indicator.RoleIndicator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;


class UserTest {

    @Test
    void getAuthoritiesAdmin() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("1234");

        Role role = new Role();
        role.setName("ROLE_ADMIN");

        user.setRoles(Set.of(role));

        List<SimpleGrantedAuthority> listRole = new ArrayList<>(Collections.singleton(new SimpleGrantedAuthority(RoleIndicator.ROLE_ADMIN.name())));
        Assertions.assertEquals(user.getAuthorities(), listRole);
    }

    @Test
    void getAuthoritiesUser() {
        User user = new User();
        user.setUsername("testUser");
        user.setPassword("1234");

        Role role = new Role();
        role.setName("ROLE_USER");

        user.setRoles(Set.of(role));

        List<SimpleGrantedAuthority> listRole = new ArrayList<>(Collections.singleton(new SimpleGrantedAuthority(RoleIndicator.ROLE_USER.name())));
        Assertions.assertEquals(user.getAuthorities(), listRole);
    }
}