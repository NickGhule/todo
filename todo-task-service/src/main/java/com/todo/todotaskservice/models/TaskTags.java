package com.todo.todotaskservice.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Objects;

@Entity
@Data
@Table(indexes = {
        @Index(name = "task_tag_index", columnList = "user_id, task_id, tagName")
})
public class TaskTags {

    @EmbeddedId
    private TaskTagId id;

    @ManyToOne
    @MapsId("taskId")
    @JoinColumn(name = "task_id", nullable = false)
    private Task task;

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        TaskTags taskTags = (TaskTags) obj;
        return Objects.equals(id, taskTags.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }


}
