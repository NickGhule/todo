package com.todo.auth.models;

import com.todo.auth.enums.AuthProvider;
import com.todo.enums.RoleName;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.JdbcType;
import org.hibernate.dialect.PostgreSQLEnumJdbcType;

import java.util.Set;
import java.util.UUID;


@Entity
@Data
@Table(name = "users")
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

    @Enumerated(EnumType.STRING)
    Set<RoleName> roles;
}
