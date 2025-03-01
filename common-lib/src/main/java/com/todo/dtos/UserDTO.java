package com.todo.dtos;

import com.todo.enums.RoleName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
public class UserDTO {
    UUID userId;
    String name;
    String email;
    Set<RoleName> roles;
}
