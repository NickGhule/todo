package com.todo.auth.mapper;

import com.todo.auth.models.User;
import com.todo.dtos.UserDTO;
import com.todo.enums.RoleName;

import java.util.Set;

public class UserDTOMapper {

    public static UserDTO toDto(User user) {
        Set<RoleName> roles = user.getRoles();
        return new UserDTO(
                user.getId(),
                user.getName(),
                user.getEmail(),
                roles
        );
    }
}
