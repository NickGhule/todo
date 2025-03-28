package com.todo.todotaskservice.dtos;

import com.todo.todotaskservice.enums.TaskPriority;
import com.todo.todotaskservice.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Data
//@AllArgsConstructor
public class TaskFilterRequestDTO {
    private TaskPriority priority;
    private TaskStatus status;
    private LocalDateTime dueDateFrom;
    private LocalDateTime dueDateTo;
    private Set<String> tags = new HashSet<>();
}
