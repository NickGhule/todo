package com.todo.auth.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdatePasswordRequestDTO {
    private String oldPassword;
    @Size(min = 6, message = "Password should be at least 6 characters long")
    private String newPassword;
}
