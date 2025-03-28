package com.todo.models;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.JoinColumn;
import lombok.Data;

@Embeddable
@Data
public class TaskTagId implements Serializable {

    @JoinColumn(foreignKey = @ForeignKey(name = "fk_user_id"))
    private Long userId;

    @JoinColumn(foreignKey = @ForeignKey(name = "fk_task_id"))
    private Long taskId;

    private String tag;

}
