package com.fincons.taskmanager.service.taskUserService.impl;

import com.fincons.taskmanager.entity.*;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.exception.RoleException;
import com.fincons.taskmanager.mapper.TaskMapper;
import com.fincons.taskmanager.repository.*;
import com.fincons.taskmanager.service.taskUserService.TaskUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
@Service
public class TaskUserServiceImpl implements TaskUserService {

    @Autowired
    private UserBoardRepository userBoardRepository;
    @Autowired
    private TaskUserRepository taskUserRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TaskMapper modelMapperTask;

    @Override
    public List<TaskUser> findTasksByUser(String email){
        validateUserByEmail(email);
        return taskUserRepository.findTasksByUser(email);
    }
    @Override
    public TaskUser createTaskUser(TaskUser taskUser) throws RoleException {

        User existingUser = validateUserByEmail(taskUser.getUser().getEmail());
        Task existingTask = validateTaskById(taskUser.getTask().getTaskId());
        checkDuplicateTaskUserExist(existingTask, existingUser);

        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        List<User> userIsEditor = existingTask.getLane().getBoard().getUsersBoards().stream()
                .filter(userBoard1 -> "EDITOR".equals(userBoard1.getRoleCode()))
                .map(UserBoard::getUser)
                .toList();
        boolean isEditorEmailValidated = userIsEditor.stream()
                .anyMatch(user -> Objects.equals(user.getEmail(), loggedUser));
        if(isEditorEmailValidated){
            TaskUser newTaskUser = new TaskUser(existingTask, existingUser);
            taskUserRepository.save(newTaskUser);
            return newTaskUser;
        }
        else{
            throw new RoleException("You need to be a EDITOR for create new relationship task-user");
        }
    }

    @Override
    public TaskUser updateTaskUser(Long taskId, String email, TaskUser taskUser) throws RoleException {

        User existingUser = validateUserByEmail(email);
        Task existingTask = validateTaskById(taskId);
        TaskUser taskUserExist = validateTaskUserRelationship(existingTask, existingUser);

        User userToUpdate = validateUserByEmail(taskUser.getUser().getEmail());
        Task taskToUpdate = validateTaskById(taskUser.getTask().getTaskId());
        validateTaskUserNotExistRelationship(taskToUpdate, userToUpdate);

        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        List<User> userIsEditor = existingTask.getLane().getBoard().getUsersBoards().stream()
                .filter(userBoard1 -> "EDITOR".equals(userBoard1.getRoleCode()))
                .map(UserBoard::getUser)
                .toList();
        boolean isEditorEmailValidated = userIsEditor.stream()
                .anyMatch(user -> Objects.equals(user.getEmail(), loggedUser));
        if(isEditorEmailValidated){
            taskUserExist.setTask(taskToUpdate);
            taskUserExist.setUser(userToUpdate);
            taskUserRepository.save(taskUserExist);
            return taskUserExist;
        }
        else{
            throw new RoleException("You need to be a EDITOR for modify new relationship task-user");
        }
    }
    @Override
    public TaskUser deleteTaskUser(Long taskId, String email) throws RoleException {
        User existingUser = validateUserByEmail(email);
        Task existingTask = validateTaskById(taskId);
        TaskUser taskUserExist = validateTaskUserRelationship(existingTask, existingUser);

        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        List<User> userIsEditor = existingTask.getLane().getBoard().getUsersBoards().stream()
                .filter(userBoard1 -> "EDITOR".equals(userBoard1.getRoleCode()))
                .map(UserBoard::getUser)
                .toList();
        boolean isEditorEmailValidated = userIsEditor.stream()
                .anyMatch(user -> Objects.equals(user.getEmail(), loggedUser));
        if(isEditorEmailValidated){
            taskUserRepository.delete(taskUserExist);
            return taskUserExist;
        }
        else{
            throw new RoleException("You need to be a EDITOR for delete the relationship task-user");
        }
    }
    private User validateUserByEmail(String email) {
        User existingUser = userRepository.findByEmail(email);
        if (Objects.isNull(existingUser)) {
            throw new ResourceNotFoundException("Error: User with EMAIL: " + email + " not found.");
        }
        return existingUser;
    }
    private Task validateTaskById(Long id) {
        Task existingTask = modelMapperTask.mapProjectionToEntity(taskRepository.findTaskByTaskIdAndActiveTrue(id));
        if (Objects.isNull(existingTask)) {
            throw new ResourceNotFoundException("Error: Task with ID: " + id + " not found.");
        }
        return existingTask;
    }
    private void checkDuplicateTaskUserExist(Task task, User user) {

        boolean taskUserExist = taskUserRepository.existsByTaskAndUser(task,user);
        if (taskUserExist) {
            throw new DuplicateException(
                    "TASK: " + task.getTaskId() + " and USER: " + user.getEmail(),
                    "TASK: " + task.getTaskId() + " and USER: " + user.getEmail());
        }
    }
    private TaskUser validateTaskUserRelationship(Task task, User user) {
        TaskUser taskUserExist = taskUserRepository.findByTaskTaskIdAndUserEmail(task.getTaskId(), user.getEmail());
        if (Objects.isNull(taskUserExist)) {
            throw new ResourceNotFoundException("Error: Relationship with ID task: " + task.getTaskId() +
                    " and USER email: " + user.getEmail() + " don't exist.");
        }
        return taskUserExist;
    }
    private void validateTaskUserNotExistRelationship(Task task, User user) {
        TaskUser taskUserExist = taskUserRepository.findByTaskTaskIdAndUserEmail(task.getTaskId(), user.getEmail());
        if (!Objects.isNull(taskUserExist)) {
            throw new ResourceNotFoundException("Error: Relationship with ID task:: " + task.getTaskId() +
                    " and USER email: " + user.getEmail() + " already exist.");
        }
    }
}
