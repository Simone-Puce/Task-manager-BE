package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findTaskByTaskCode(String code);
    Task findTaskByTaskCodeAndActiveTrue(String code);
    List<Task> findAllByActiveTrue();
    boolean existsByTaskCode(String code);
    boolean existsByTaskCodeAndActiveTrue(String taskCode);
    @Query(value = "SELECT AUTO_INCREMENT " +
            "FROM information_schema.TABLES " +
            "WHERE TABLE_SCHEMA = 'task-manager' " +
            "AND TABLE_NAME = 'task'",
            nativeQuery = true)
    Long getNextSeriesId();
    @Query("SELECT CONCAT(UPPER(t.taskName), '-', t.id) FROM Task t")
    List<String> getTaskNameAndIdConcatenated();
}
