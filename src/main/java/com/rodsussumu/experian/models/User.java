package com.rodsussumu.experian.models;

import com.rodsussumu.experian.indicator.RoleIndicator;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "tb_users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    private String username;

    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "tb_users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<SimpleGrantedAuthority> listRole = new ArrayList<>();
        for (Role role : this.roles) {
            if (RoleIndicator.ADMIN.toString().equals(role.getName())) listRole.add(new SimpleGrantedAuthority("ROLE_" + RoleIndicator.ADMIN.toString()));
            else listRole.add(new SimpleGrantedAuthority("ROLE_" + RoleIndicator.USER.toString()));
        }
        return listRole;
    }
}
