package com.todo.todotaskservice.repositories;

import com.todo.todotaskservice.models.TaskTagId;
import com.todo.todotaskservice.models.TaskTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TaskTagsRepository extends JpaRepository<TaskTags, TaskTagId> {
    void removeById(TaskTagId id);

    @Query("SELECT tags.id.tagName FROM TaskTags tags " +
            "WHERE tags.id.userId = :userId AND tags.id.taskId = :taskId")
    List<String> findAllByUserIdAndTaskId(@Param("userId") UUID userId, @Param("taskId") UUID taskId);
}
