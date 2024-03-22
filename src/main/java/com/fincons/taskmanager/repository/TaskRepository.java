package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.projection.TaskProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    TaskProjection findTaskByTaskIdAndActiveTrue(Long id);
    List<TaskProjection> findAllByActiveTrue();
    boolean existsByTaskIdAndActiveTrue(Long id);
}
