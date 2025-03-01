package com.todo.auth.model;

import com.todo.auth.enums.AuthProvider;
import jakarta.persistence.*;

import java.util.Set;
import java.util.UUID;


@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    UUID id;
    String name;

    @Column(unique = true, nullable = false)
    String email;

    String password;

    @Enumerated(EnumType.STRING)
    AuthProvider provider;

    String providerId;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    Set<Role> roles;
}
