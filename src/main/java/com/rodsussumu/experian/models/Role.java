package com.rodsussumu.experian.models;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tb_roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Long id;

    private String name;
}
