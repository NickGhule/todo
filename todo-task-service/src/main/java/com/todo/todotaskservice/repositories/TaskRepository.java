package com.todo.todotaskservice.repositories;

import com.todo.todotaskservice.dtos.TaskFilterRequestDTO;
import com.todo.todotaskservice.enums.TaskPriority;
import com.todo.todotaskservice.enums.TaskStatus;
import com.todo.todotaskservice.models.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.*;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    Page<Task> findAllByUserId(UUID userId, Pageable pageable);
    Optional<List<Task>> findTaskByUserIdAndTitle(UUID userId, String title);
    Optional<List<Task>> findByUserIdAndDueDate(UUID userId, Date dueDate);
    Optional<List<Task>> findAllByUserId(UUID userId);
    Optional<List<Task>> findByUserIdAndDueDateBetween(UUID userId, Date startDate, Date endDate);
    Optional<List<Task>> findTaskByUserIdAndStatus(UUID userId, TaskStatus status);

    @Query("SELECT t FROM Task t " +
            "LEFT JOIN FETCH t.taskTags " +
            "WHERE t.id IN ( " +
            "SELECT tt.id.taskId FROM TaskTags tt " +
            "WHERE tt.id.userId = :userId " +
            "AND tt.id.tagName IN :tags " +
            ")"
    )
    List<Task> findByUserIdAndAnyTags(UUID userId, Set<String> tags);

    void deleteTaskByUserIdAndId(UUID userId, UUID taskId);

    Task findTaskByUserIdAndId(UUID userId, UUID taskId);

    List<Task> findAllByUserIdAndPriority(UUID userId, TaskPriority taskPriority);
    List<Task> findAllByUserIdAndStatus(UUID userId, TaskStatus status);
    List<Task> findAllByUserIdAndDueDateBetween(UUID userId, LocalDateTime startDueDate, LocalDateTime endDueDate);

    @Query("SELECT t FROM Task t " +
            "LEFT JOIN FETCH t.taskTags " +
            "WHERE (:#{#taskFilters.priority} IS NULL OR t.priority = :#{#taskFilters.priority}) " +
            "AND (:#{#taskFilters.status} IS NULL OR t.status = :#{#taskFilters.status}) " +
            "AND (:#{#taskFilters.dueDateFrom} IS NULL OR t.dueDate >= :#{#taskFilters.dueDateFrom}) " +
            "AND (:#{#taskFilters.dueDateTo} IS NULL OR t.dueDate <= :#{#taskFilters.dueDateTo}) " +
            "AND ( :#{#taskFilters.tags.size()} = 0 OR t.id IN ( " +
            "   SELECT tt.id.taskId FROM TaskTags tt " +
            "   WHERE tt.id.userId = :userId " +
            "   AND tt.id.tagName IN :#{#taskFilters.tags} " +
            "   GROUP BY tt.id.taskId " +
            "   HAVING COUNT(DISTINCT tt.id.tagName) = :#{#taskFilters.tags.size()} " +
            "))"
    )
    List<Task> findFiltered(UUID userId, @Param("taskFilters") TaskFilterRequestDTO taskFilters);
}
