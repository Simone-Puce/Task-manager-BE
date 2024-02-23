package com.fincons.taskmanager.service.taskService.impl;

import com.fincons.taskmanager.dto.TaskDTO;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.TaskRepository;
import com.fincons.taskmanager.service.taskService.TaskService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Objects;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Task getTaskByCode(String code) {
        return null;
    }

    @Override
    public List<Task> getAllTasks() {
        return null;
    }

    @Override
    public Task createTask(Task task) {
        return null;
    }

    @Override
    public Task updateTaskByCode(String code, Task task) {
        return null;
    }

    @Override
    public void deleteTaskByCode(String code) {

    }
    public Task validateTaskByCode(String code) {
        Task existingCode = taskRepository.findTaskByTaskCode(code);

        if (Objects.isNull(existingCode)) {
            throw new ResourceNotFoundException("Error: Task with CODE: " + code + " not found.");
        }
        return existingCode;
    }
    public void validateTaskFields(TaskDTO taskDTO) {
        if (Strings.isEmpty(taskDTO.getTaskCode()) ||
                Strings.isEmpty(taskDTO.getName()) ||
                Strings.isEmpty(taskDTO.getDescription()) ||
                Strings.isEmpty(taskDTO.getStatus()) ||
                Strings.isEmpty(taskDTO.getBoardCode())) {
            throw new IllegalArgumentException("Error: The fields of the task can't be null or empty.");
        }
    }
    private void checkForDuplicateTask(String code) {
        Task taskByCode = taskRepository.findTaskByTaskCode(code);
        if (!Objects.isNull(taskByCode)) {
            throw new DuplicateException("CODE: " + code, "CODE: " + taskByCode.getTaskCode());
        }
    }
}
