package com.todo.todotaskservice.mapper;

import com.todo.todotaskservice.dtos.TaskDTO;
import com.todo.todotaskservice.models.Task;

public class TaskDTOMapper {
    public static TaskDTO toDTO(Task task) {
        return new TaskDTO(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getPriority(),
                task.getStatus(),
                task.getDueDate(),
                task.getTags()
        );
    }
}
