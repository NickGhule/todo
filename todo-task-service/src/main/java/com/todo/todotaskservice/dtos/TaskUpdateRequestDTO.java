package com.todo.todotaskservice.dtos;

import com.todo.todotaskservice.enums.TaskPriority;
import com.todo.todotaskservice.enums.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class TaskUpdateRequestDTO {
    @NotBlank
    private UUID id;
    private String title;
    private String description;
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDateTime dueDate;
}
