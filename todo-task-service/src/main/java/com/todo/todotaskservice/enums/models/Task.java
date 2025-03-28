package com.todo.models;

import com.todo.enums.TaskStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.Set;
import java.util.UUID;

@Entity
@Data
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String title;

    @Column(length = 1000)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status = TaskStatus.PENDING;

    private Date dueDate;

    @Transient
    private Set<String> tags;

}
