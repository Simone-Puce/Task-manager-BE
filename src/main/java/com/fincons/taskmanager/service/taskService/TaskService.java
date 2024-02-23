package com.fincons.taskmanager.service.taskService;

import com.fincons.taskmanager.entity.Task;

import java.util.List;

public interface TaskService{

    Task getTaskByCode(String code);
    List<Task> getAllTasks();
    Task createTask(Task task);
    Task updateTaskByCode(String code, Task task);
    void deleteTaskByCode(String code);
}
