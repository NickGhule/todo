package com.todo.todotaskservice.services;

import com.todo.todotaskservice.dtos.TaskDTO;
import com.todo.todotaskservice.enums.TaskPriority;
import com.todo.todotaskservice.mapper.TaskDTOMapper;
import com.todo.todotaskservice.models.Task;
import com.todo.todotaskservice.repositories.TaskRepository;
import com.todo.todotaskservice.repositories.TaskTagsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TagsService {

    private final TaskTagsRepository taskTagsRepository;
    private final TaskRepository taskRepository;

    public TagsService(TaskTagsRepository taskTagsRepository, TaskRepository taskRepository) {
        this.taskTagsRepository = taskTagsRepository;
        this.taskRepository = taskRepository;
    }

    public List<TaskDTO> getTasksForUserByTagNames(UUID userId, Set<String> tags) {
        List<Task> tasks = taskRepository.findByUserIdAndAnyTags(userId, tags);
        if (tasks.isEmpty()) {
            return new ArrayList<>();
        }
        tags = tags.stream().map(String::toLowerCase).collect(Collectors.toUnmodifiableSet());
        Set<String> finalTags = tags;
        tasks = tasks.stream().filter(task -> task.getTags().containsAll(finalTags)).toList();
        return tasks.stream().map(TaskDTOMapper::toDTO).toList();
    }


}
