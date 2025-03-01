package com.todo.dtos;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ErrorDTO {
    private LocalDateTime timestamp;
    private String error;
    private String message;
    private int status;
}

