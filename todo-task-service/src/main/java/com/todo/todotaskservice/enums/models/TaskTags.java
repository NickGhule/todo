package com.todo.models;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(indexes = {
        @Index(name = "task_tag_index", columnList = "user_id, task_id, tag")
})
public class TaskTags {

    @EmbeddedId
    private TaskTagId id;

}
