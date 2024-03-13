package com.fincons.taskmanager.service.taskService.impl;

import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.TaskRepository;
import com.fincons.taskmanager.repository.TaskUserRepository;
import com.fincons.taskmanager.service.boardService.impl.BoardServiceImpl;
import com.fincons.taskmanager.service.taskService.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import java.util.Objects;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private BoardServiceImpl boardServiceImpl;
    @Autowired
    private TaskUserRepository taskUserRepository;

    @Override
    public Task getTaskById(Long taskId) {
        existingTaskById(taskId);
        return taskRepository.findTaskIdForAttachmentsAllTrue(taskId);
    }

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAllForAttachmentsAllTrue();
    }

    @Override
    public Task createTask(Task task) {
        Board board = boardServiceImpl.validateBoardById(task.getBoard().getBoardId());
        task.setBoard(board);
        task.setActive(true);
        taskRepository.save(task);
        return task;
    }

    @Override
    public Task updateTaskById(Long taskId, Task task) {
        existingTaskById(taskId);
        Task taskExisting = taskRepository.findTaskIdForAttachmentsAllTrue(taskId);
        taskExisting.setTaskName(task.getTaskName());
        taskExisting.setStatus(task.getStatus());
        taskExisting.setDescription(task.getDescription());
        Board board = boardServiceImpl.validateBoardById(task.getBoard().getBoardId());
        taskExisting.setBoard(board);
        taskRepository.save(taskExisting);
        return taskExisting;
    }
    @Override
    @Transactional
    public void deleteTaskById(Long taskId) {
        Task task = validateTaskById(taskId);
        task.setActive(false);
        taskRepository.save(task);
        taskUserRepository.deleteByTask(task);
    }
    public Task validateTaskById(Long id) {
        Task existingId = taskRepository.findTaskByTaskIdAndActiveTrue(id);
        if (Objects.isNull(existingId)) {
            throw new ResourceNotFoundException("Error: Task with ID: " + id + " not found.");
        }
        return existingId;
    }
    public void existingTaskById(Long id){
        boolean existingId = taskRepository.existsByTaskIdAndActiveTrue(id);
        if(!existingId){
            throw new ResourceNotFoundException("Error: Task with ID: " + id + " not found");
        }
    }
}
