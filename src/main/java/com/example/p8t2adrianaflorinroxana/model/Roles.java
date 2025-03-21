package com.example.p8t2adrianaflorinroxana.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Entity
@Table(name = "Roles")
public class Roles implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private long id;

    @Column(name = "role_name",nullable = false, length = 20)
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private Collection<Users> users;

    @Override
    public String getAuthority() {
        return roleName;
    }
}
