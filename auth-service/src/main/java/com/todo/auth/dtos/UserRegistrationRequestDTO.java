package com.todo.auth.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationRequestDTO {
    @NotBlank
    String firstName;

    @NotBlank
    String lastName;

    @Email
    @NotBlank
    String email;

    @NotBlank
    @Size(min = 6, message = "Password should be at least 6 characters long")
    String password;
}
