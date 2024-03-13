package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query(value = "SELECT t " +
            "FROM Task t " +
            "JOIN FETCH t.attachments a " +
            "WHERE t.taskId = :taskId " +
            "AND t.active = true " +
            "AND a.active = true ")
    Task findTaskIdForAttachmentsAllTrue(@Param("taskId")Long taskId);
    @Query(value = "SELECT t " +
            "FROM Task t " +
            "JOIN FETCH t.attachments a " +
            "WHERE t.active = true " +
            "AND a.active = true "
    )
    List<Task> findAllForAttachmentsAllTrue();
    Task findTaskByTaskId(Long id);
    Task findTaskByTaskIdAndActiveTrue(Long id);
    boolean existsByTaskIdAndActiveTrue(Long id);
}
