package com.fincons.taskmanager.service.taskService;

import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.exception.RoleException;

import java.util.List;

public interface TaskService{

    Task getTaskById(Long taskId);
    List<Task> getAllTasks();
    Task createTask(Task task);
    Task updateTaskById(Long taskId, Task task) throws RoleException;
    void deleteTaskById(Long taskId) throws RoleException;
}
