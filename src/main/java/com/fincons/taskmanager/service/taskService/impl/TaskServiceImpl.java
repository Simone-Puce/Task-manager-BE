package com.fincons.taskmanager.service.taskService.impl;

import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.TaskRepository;
import com.fincons.taskmanager.service.boardService.impl.BoardServiceImpl;
import com.fincons.taskmanager.service.taskService.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.util.Objects;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private BoardServiceImpl boardServiceImpl;

    @Override
    public Task getTaskByCode(String taskCode) {
        return validateTaskByCode(taskCode);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task createTask(Task task) {
        checkForDuplicateTask(task.getTaskCode());

        Board board = boardServiceImpl.validateBoardByCode(task.getBoard().getBoardCode());
        task.setBoard(board);
        taskRepository.save(task);
        return task;
    }

    @Override
    public Task updateTaskByCode(String taskCode, Task task) {
        List<Task> tasks = taskRepository.findAll();
        Task taskExisting = validateTaskByCode(taskCode);

        List<Task> tasksExcludingSelectedTask = new ArrayList<>();

        for(Task t : tasks){
            if (!Objects.equals(t.getTaskCode(), taskCode)){
                tasksExcludingSelectedTask.add(t);
            }
        }
        taskExisting.setTaskCode(task.getTaskCode());
        taskExisting.setTaskName(task.getTaskName());
        taskExisting.setStatus(task.getStatus());
        taskExisting.setDescription(task.getDescription());

        Board board = boardServiceImpl.validateBoardByCode(task.getBoard().getBoardCode());
        taskExisting.setBoard(board);

        if(tasksExcludingSelectedTask.isEmpty()){
            taskRepository.save(taskExisting);
        } else {
            for(Task t : tasksExcludingSelectedTask){
                if(t.getTaskCode().equals(taskExisting.getTaskCode())){
                    throw new DuplicateException("CODE: " + taskCode, "CODE: " + task.getTaskCode());
                }
            }
            taskRepository.save(taskExisting);
        }

        return taskExisting;
    }

    @Override
    public void deleteTaskByCode(String taskCode) {
        Task task = validateTaskByCode(taskCode);
        taskRepository.deleteById(task.getId());
    }
    public Task validateTaskByCode(String code) {
        Task existingCode = taskRepository.findTaskByTaskCode(code);

        if (Objects.isNull(existingCode)) {
            throw new ResourceNotFoundException("Error: Task with CODE: " + code + " not found.");
        }
        return existingCode;
    }
    private void checkForDuplicateTask(String taskCode) {
        Task taskByCode = taskRepository.findTaskByTaskCode(taskCode);
        if (!Objects.isNull(taskByCode)) {
            throw new DuplicateException("CODE: " + taskCode, "CODE: " + taskByCode.getTaskCode());
        }
    }
}
