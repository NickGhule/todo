package com.todo.todotaskservice.dtos;

import com.todo.todotaskservice.enums.TaskPriority;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;

@Data
public class TaskCreateRequestDTO {
    private String title;
    private String description;
    private TaskPriority priority;
    private LocalDateTime dueDate;
    private Set<String> tags;
}
