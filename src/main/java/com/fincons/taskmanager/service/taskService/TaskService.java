package com.fincons.taskmanager.service.taskService;

import com.fincons.taskmanager.entity.Task;

import java.util.List;

public interface TaskService{

    Task getTaskById(Long taskId);
    List<Task> getAllTasks();
    Task createTask(Task task);
    Task updateTaskById(Long taskId, Task task);
    void deleteTaskById(Long taskId);
}
