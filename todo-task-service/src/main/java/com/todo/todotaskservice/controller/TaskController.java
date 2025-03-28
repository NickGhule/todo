package com.todo.todotaskservice.controller;

import com.todo.todotaskservice.dtos.TaskCreateRequestDTO;
import com.todo.todotaskservice.dtos.TaskDTO;
import com.todo.todotaskservice.dtos.TaskFilterRequestDTO;
import com.todo.todotaskservice.dtos.TaskUpdateRequestDTO;
import com.todo.todotaskservice.enums.TaskPriority;
import com.todo.todotaskservice.enums.TaskStatus;
import com.todo.todotaskservice.services.TagsService;
import com.todo.todotaskservice.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    private final TagsService tagsService;

    public TaskController(TaskService taskService, TagsService tagsService) {
        this.taskService = taskService;
        this.tagsService = tagsService;
    }

    @GetMapping()
    public ResponseEntity<List<TaskDTO>> getTasksByUserId(@AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaim("userId"));
        List<TaskDTO> tasks = taskService.getTasksByUserId(userId);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<TaskDTO>> getTasksByUserIdWithFilter(@AuthenticationPrincipal Jwt jwt, @RequestBody TaskFilterRequestDTO taskFilters) {
        UUID userId = UUID.fromString(jwt.getClaim("userId"));
        List<TaskDTO> tasks = taskService.getTasksByUserWithFilters(userId, taskFilters);
        return ResponseEntity.ok(tasks);
    }

    @PostMapping()
    public ResponseEntity<TaskDTO> createTaskForUser(@RequestBody TaskCreateRequestDTO taskDTO, @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaim("userId"));
        TaskDTO createdTask = taskService.createTask(userId, taskDTO);
        return ResponseEntity.ok(createdTask);
    }

    @PutMapping
    public ResponseEntity<TaskDTO> updateTask(@RequestBody TaskUpdateRequestDTO taskDTO, @AuthenticationPrincipal Jwt jwt) {
        UUID userId = UUID.fromString(jwt.getClaim("userId"));
        TaskDTO updatedTask = taskService.updateTask(userId, taskDTO);
        return updatedTask != null ? ResponseEntity.ok(updatedTask) : ResponseEntity.notFound().build();
    }

    @GetMapping("/tags")
    public ResponseEntity<List<TaskDTO>> getTasksByTags(@RequestBody Map<String, Set<String>> requestBody, @AuthenticationPrincipal Jwt jwt) {
        Set<String> tags = requestBody.get("tags");
        if (tags == null) {
            return ResponseEntity.badRequest().build();
        }
        UUID userId = UUID.fromString(jwt.getClaim("userId"));
        List<TaskDTO> tasks = tagsService.getTasksForUserByTagNames(userId, tags);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/priority")
    public ResponseEntity<List<TaskDTO>> getTasksByPriority(@RequestBody TaskPriority taskPriority, @AuthenticationPrincipal Jwt jwt) {

        UUID userId = UUID.fromString(jwt.getClaim("userId"));
        List<TaskDTO> tasks = taskService.getTasksByUserIdAndPriority(userId, taskPriority);
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/status")
    public ResponseEntity<List<TaskDTO>> getTasksByStatus(@AuthenticationPrincipal Jwt jwt, @RequestParam TaskStatus status) {
        UUID userId = UUID.fromString(jwt.getClaim("userId"));
        List<TaskDTO> tasks = taskService.getTasksByUserIdAndStatus(userId, status);
        return ResponseEntity.ok(tasks);
    }


}

