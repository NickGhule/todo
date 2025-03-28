package com.todo.todotaskservice.models;

import com.todo.todotaskservice.enums.TaskPriority;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import com.todo.todotaskservice.enums.TaskStatus;

@Entity
@Data
@Table(indexes = {
        @Index(name = "task_user_index", columnList = "user_id, id"),
})
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column
    private TaskPriority priority;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.PENDING;

    @Column
    private LocalDateTime dueDate;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<TaskTags> taskTags;

    @Transient
    public Set<String> getTags() {
        return taskTags != null ? taskTags.stream()
                .map(taskTag -> taskTag.getId().getTagName())
                .collect(Collectors.toSet()) : Set.of();
    }

}
