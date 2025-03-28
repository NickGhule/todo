package com.todo.todotaskservice.services;

import com.todo.todotaskservice.dtos.TaskCreateRequestDTO;
import com.todo.todotaskservice.dtos.TaskDTO;
import com.todo.todotaskservice.dtos.TaskFilterRequestDTO;
import com.todo.todotaskservice.dtos.TaskUpdateRequestDTO;
import com.todo.todotaskservice.enums.TaskPriority;
import com.todo.todotaskservice.enums.TaskStatus;
import com.todo.todotaskservice.exceptions.TaskNotFoundException;
import com.todo.todotaskservice.mapper.TaskDTOMapper;
import com.todo.todotaskservice.models.Task;
import com.todo.todotaskservice.models.TaskTagId;
import com.todo.todotaskservice.models.TaskTags;
import com.todo.todotaskservice.repositories.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskDTO> getTasksByUserId(UUID userId) {
        List<Task> tasks = taskRepository.findAllByUserId(userId).orElse(new ArrayList<>());
        return tasks.stream().map(TaskDTOMapper::toDTO).toList();
    }

    @Transactional
    public TaskDTO createTask(UUID userId, TaskCreateRequestDTO taskDTO) {
        Task task = new Task();
        task.setUserId(userId);
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setPriority(taskDTO.getPriority());
        task.setStatus(TaskStatus.PENDING);
        task.setDueDate(taskDTO.getDueDate());
        task.setTaskTags( taskDTO.getTags() != null ?
                taskDTO.getTags().stream()
                        .map(tagName -> {
                            TaskTagId taskTagId = new TaskTagId();
                            taskTagId.setUserId(userId);
                            taskTagId.setTagName(tagName);
                            taskTagId.setTaskId(task.getId()); // This is null before saving, so use `task` itself.

                            TaskTags taskTag = new TaskTags();
                            taskTag.setId(taskTagId);
                            taskTag.setTask(task);
                            return taskTag;
                        }).collect(Collectors.toSet()) : new HashSet<>()
        );
//        Task createdTask = taskRepository.save(task);


        return TaskDTOMapper.toDTO(taskRepository.save(task));

    }

    @Transactional
    public void removeTask(UUID userId, UUID taskId) {
        taskRepository.deleteTaskByUserIdAndId(userId, taskId);
    }

    @Transactional
    public TaskDTO updateTask(UUID userId, TaskUpdateRequestDTO taskDTO) {
        Task task = taskRepository.findTaskByUserIdAndId(userId, taskDTO.getId());
        if (task == null) {
            throw new TaskNotFoundException(String.format("No task found for userId: {} with taskId: {}", userId, taskDTO.getId()));
        }
        task.setTitle(taskDTO.getTitle());
        task.setDescription(taskDTO.getDescription());
        task.setPriority(taskDTO.getPriority());
        task.setStatus(taskDTO.getStatus());
        task.setDueDate(taskDTO.getDueDate());
        return TaskDTOMapper.toDTO(taskRepository.save(task));
    }

    public List<TaskDTO> getTasksByUserIdAndPriority(UUID userId, TaskPriority taskPriority) {
        List<Task> tasks = taskRepository.findAllByUserIdAndPriority(userId, taskPriority);
        if (tasks.isEmpty()) {
            return new ArrayList<>();
        }
        return tasks.stream().map(TaskDTOMapper::toDTO).toList();
    }

    public List<TaskDTO> getTasksByUserIdAndStatus(UUID userId, TaskStatus status) {
        List<Task> tasks = taskRepository.findAllByUserIdAndStatus(userId, status);
        if (tasks.isEmpty()) {
            return new ArrayList<>();
        }
        return tasks.stream().map(TaskDTOMapper::toDTO).toList();
    }

    public List<TaskDTO> getTasksByUserWithFilters(UUID userId, TaskFilterRequestDTO taskFilters) {
        List<Task> tasks = taskRepository.findFiltered(userId, taskFilters);
        return tasks.stream().map(TaskDTOMapper::toDTO).toList();
    }
}
