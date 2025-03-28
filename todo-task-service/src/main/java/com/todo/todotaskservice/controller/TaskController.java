package com.todo.todotaskservice.controller;

import com.todo.todotaskservice.dtos.TaskDTO;
import com.todo.todotaskservice.services.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    private final TaskService taskService;
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }
    @GetMapping("{userId}"})
    public ResponseEntity<TaskDTO> getTasksByUserId(@PathVariable("userId") String userId) {
        List<TaskDTO> tasks = taskServ
    }


}
