package com.fincons.taskmanager.service.taskService.impl;

import com.fincons.taskmanager.entity.Lane;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.entity.UserBoard;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.exception.RoleException;
import com.fincons.taskmanager.mapper.TaskMapper;
import com.fincons.taskmanager.repository.AttachmentRepository;
import com.fincons.taskmanager.repository.BoardRepository;
import com.fincons.taskmanager.repository.TaskRepository;
import com.fincons.taskmanager.repository.TaskUserRepository;
import com.fincons.taskmanager.projection.TaskProjection;
import com.fincons.taskmanager.service.laneService.impl.LaneServiceImpl;
import com.fincons.taskmanager.service.taskService.TaskService;
import com.fincons.taskmanager.service.userBoardService.UserBoardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import java.util.Objects;

@Service
public class TaskServiceImpl implements TaskService {

    private static final String EDITOR = "EDITOR";
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private LaneServiceImpl laneServiceImpl;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserBoardService userBoardService;
    @Autowired
    private TaskUserRepository taskUserRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private TaskMapper modelMapperTask;
    private static final Logger log = LogManager.getLogger(TaskServiceImpl.class);

    @Override
    public Task getTaskById(Long taskId) {
        existingTaskById(taskId);
        TaskProjection taskProjection = taskRepository.findTaskByTaskIdAndActiveTrue(taskId);
        return modelMapperTask.mapProjectionToEntity(taskProjection);
    }

    @Override
    public List<Task> getAllTasks(){
        return sortTaskList(modelMapperTask.mapProjectionsToEntities(taskRepository.findAllByActiveTrue()));
    }

    @Override
    public Task createTask(Task task) {
        Lane lane = laneServiceImpl.validateLaneById(task.getLane().getLaneId());
        task.setLane(lane);
        task.setActive(true);
        taskRepository.save(task);
        log.info("New Task saved in the repository with ID {}.", task.getTaskId());
        return task;
    }

    @Override
    public Task updateTaskById(Long taskId, Task task) throws RoleException {
        existingTaskById(taskId);
        Task taskExisting = modelMapperTask.mapProjectionToEntity(taskRepository.findTaskByTaskIdAndActiveTrue(taskId));

        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean userAssociated = taskExisting.getTasksUsers().stream()
                        .anyMatch(taskUser -> Objects.equals(taskUser.getUser().getEmail(), loggedUser));
        Lane laneToGetBoard = taskExisting.getLane();

        long boardId = laneToGetBoard.getBoard().getBoardId();
        List<UserBoard> userBoards = userBoardService.findBoardsByUser(loggedUser);
        List<UserBoard> userInBoard = userBoards.stream().filter(userBoardToCheck -> userBoardToCheck.getBoard().getBoardId() == boardId).toList();

        UserBoard userBoard = userInBoard.get(0);
        boolean userIsEditor = false;
        String roleCode = userBoard.getRoleCode();
        log.info(userBoard.getRoleCode());
        if(EDITOR.equals(roleCode)){
            userIsEditor = true;
        }

        if(userInBoard.isEmpty()){
            throw new RoleException("something missing.");
        }
        if(!userAssociated && !userIsEditor){
            throw new RoleException("You are not associated with this task and cannot update it.");
        }
        taskExisting.setTaskName(task.getTaskName());
        taskExisting.setDescription(task.getDescription());
        Lane lane = laneServiceImpl.validateLaneById(task.getLane().getLaneId());
        if(Objects.equals(lane.getBoard(), taskExisting.getLane().getBoard())){
            taskExisting.setLane(lane);
            taskRepository.save(taskExisting);
            log.info("Updated Task in the repository with ID {}.", taskExisting.getTaskId());
            return taskExisting;
        }
        else {
            throw new IllegalArgumentException("You can't choose lane of another BOARD!");
        }
    }

    @Override
    @Transactional
    public void deleteTaskById(Long taskId) throws RoleException {
        Task task = validateTaskById(taskId);

        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean userAssociated = task.getTasksUsers().stream()
                .anyMatch(taskUser -> Objects.equals(taskUser.getUser().getEmail(), loggedUser));
        if (!userAssociated){
            throw new RoleException("You are not associated with this task and cannot delete it.");
        }
        task.setActive(false);
        task.getAttachments().forEach(attachment -> {
            log.info("Attachment with ID {} deleted from the repository.", attachment.getAttachmentId());
            attachmentRepository.delete(attachment);
        });
        taskRepository.save(task);
        taskUserRepository.deleteByTask(task);
        log.info("Task with ID {} deleted from the repository.", task.getTaskId());
    }
    public Task validateTaskById(Long id) {
        Task existingTask = modelMapperTask.mapProjectionToEntity(taskRepository.findTaskByTaskIdAndActiveTrue(id));
        if (Objects.isNull(existingTask)) {
            throw new ResourceNotFoundException("Error: Task with ID: " + id + " not found.");
        }
        log.info("Task retrieved from repository with ID: {}", existingTask.getTaskId());
        return existingTask;
    }
    public void existingTaskById(Long id){
        boolean existingId = taskRepository.existsByTaskIdAndActiveTrue(id);
        if(!existingId){
            throw new ResourceNotFoundException("Error: Task with ID: " + id + " not found");
        }
        log.info("Task retrieved from repository with ID: {} exists", id);
    }
    private List<Task> sortTaskList(List<Task> tasks){
        List<Task> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort(Comparator.comparing(Task::getTaskName)
                .thenComparing(Task::getCreatedDate));
        return sortedTasks;
    }
}
