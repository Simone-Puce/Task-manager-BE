package com.fincons.taskmanager.service.taskService;

import com.fincons.taskmanager.dto.TaskDTO;
import com.fincons.taskmanager.entity.Task;

import java.util.List;

public interface TaskService{

    Task getTaskByCode(String taskCode);
    List<Task> getAllTasks();
    Task createTask(Task task);
    Task updateTaskByCode(String taskCode, Task task);
    void deleteTaskByCode(String taskCode);
}
