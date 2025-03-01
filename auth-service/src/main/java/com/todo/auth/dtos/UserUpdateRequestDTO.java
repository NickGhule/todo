package com.todo.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.UUID;

@Data
public class UserUpdateRequestDTO {
    @NotBlank
    UUID userId;

    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    @Email
    @NotBlank
    String email;

    String password;
}
