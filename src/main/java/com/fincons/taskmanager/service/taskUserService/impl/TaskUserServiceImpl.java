package com.fincons.taskmanager.service.taskUserService.impl;

import com.fincons.taskmanager.entity.*;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.*;
import com.fincons.taskmanager.service.taskUserService.TaskUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
@Service
public class TaskUserServiceImpl implements TaskUserService {

    @Autowired
    private TaskUserRepository taskUserRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public List<TaskUser> findTasksByUser(String email){
        validateUserByEmail(email);
        return taskUserRepository.findTasksByUser(email);
    }
    @Override
    public TaskUser createTaskUser(TaskUser taskUser) {

        User existingUser = validateUserByEmail(taskUser.getUser().getEmail());
        Task existingTask = validateTaskByCode(taskUser.getTask().getTaskCode());

        checkDuplicateTaskUserExist(existingTask, existingUser);
        TaskUser newTaskUser = new TaskUser(existingTask, existingUser);
        taskUserRepository.save(newTaskUser);
        return newTaskUser;
    }
    @Override
    public TaskUser updateTaskUser(String taskCode, String email, TaskUser taskUser) {

        User existingUser = validateUserByEmail(email);
        Task existingTask = validateTaskByCode(taskCode);
        TaskUser taskUserExist = validateTaskUserRelationship(existingTask, existingUser);

        User userToUpdate = validateUserByEmail(taskUser.getUser().getEmail());
        Task taskToUpdate = validateTaskByCode(taskUser.getTask().getTaskCode());
        validateTaskUserNotExistRelationship(taskToUpdate, userToUpdate);

        taskUserExist.setTask(taskToUpdate);
        taskUserExist.setUser(userToUpdate);
        taskUserRepository.save(taskUserExist);

        return taskUserExist;
    }
    @Override
    public TaskUser deleteTaskUser(String taskCode, String email) {
        User existingUser = validateUserByEmail(email);
        Task existingTask = validateTaskByCode(taskCode);
        TaskUser taskUserExist = validateTaskUserRelationship(existingTask, existingUser);
        taskUserRepository.delete(taskUserExist);
        return taskUserExist;
    }

    private User validateUserByEmail(String email) {
        User existingUser = userRepository.findByEmail(email);
        if (Objects.isNull(existingUser)) {
            throw new ResourceNotFoundException("Error: User with EMAIL: " + email + " not found.");
        }
        return existingUser;
    }
    private Task validateTaskByCode(String code) {
        Task existingTask = taskRepository.findTaskByTaskCodeAndActiveTrue(code);
        if (Objects.isNull(existingTask)) {
            throw new ResourceNotFoundException("Error: Task with CODE: " + code + " not found.");
        }
        return existingTask;
    }
    private void checkDuplicateTaskUserExist(Task task, User user) {

        boolean taskUserExist = taskUserRepository.existsByTaskAndUser(task,user);
        if (taskUserExist) {
            throw new DuplicateException(
                    "TASK: " + task.getTaskCode() + " and USER: " + user.getEmail(),
                    "TASK: " + task.getTaskCode() + " and USER: " + user.getEmail());
        }
    }
    private TaskUser validateTaskUserRelationship(Task task, User user) {
        TaskUser taskUserExist = taskUserRepository.findByTaskTaskCodeAndUserEmail(task.getTaskCode(), user.getEmail());
        if (Objects.isNull(taskUserExist)) {
            throw new ResourceNotFoundException("Error: Relationship with CODE task:: " + task.getTaskCode() +
                    " and USER email: " + user.getEmail() + " don't exist.");
        }
        return taskUserExist;
    }
    private void validateTaskUserNotExistRelationship(Task task, User user) {
        TaskUser taskUserExist = taskUserRepository.findByTaskTaskCodeAndUserEmail(task.getTaskCode(), user.getEmail());
        if (!Objects.isNull(taskUserExist)) {
            throw new ResourceNotFoundException("Error: Relationship with CODE task:: " + task.getTaskCode() +
                    " and USER email: " + user.getEmail() + " already exist.");
        }
    }
}
