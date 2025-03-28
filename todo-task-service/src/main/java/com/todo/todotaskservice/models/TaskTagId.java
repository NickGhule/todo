package com.todo.todotaskservice.models;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import lombok.Data;

@Embeddable
@Data
public class TaskTagId implements Serializable {

    @JoinColumn(foreignKey = @ForeignKey(name = "fk_user_id"))
    private UUID userId;

    @JoinColumn(foreignKey = @ForeignKey(name = "fk_task_id"))
    private UUID taskId;

    private String tagName;

}
