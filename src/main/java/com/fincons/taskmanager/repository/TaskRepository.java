package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Task findTaskByTaskIdAndActiveTrue(Long id);
    List<Task> findAllByActiveTrue();
    boolean existsByTaskIdAndActiveTrue(Long id);
}
