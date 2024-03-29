package com.fincons.taskmanager.service.taskService.impl;

import com.fincons.taskmanager.entity.Lane;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.mapper.TaskMapper;
import com.fincons.taskmanager.repository.AttachmentRepository;
import com.fincons.taskmanager.repository.TaskRepository;
import com.fincons.taskmanager.repository.TaskUserRepository;
import com.fincons.taskmanager.projection.TaskProjection;
import com.fincons.taskmanager.service.laneService.impl.LaneServiceImpl;
import com.fincons.taskmanager.service.taskService.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import java.util.Objects;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private LaneServiceImpl laneServiceImpl;
    @Autowired
    private TaskUserRepository taskUserRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private TaskMapper modelMapperTask;

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
        return task;
    }
    @Override
    public Task updateTaskById(Long taskId, Task task) {
        existingTaskById(taskId);
        Task taskExisting = modelMapperTask.mapProjectionToEntity(taskRepository.findTaskByTaskIdAndActiveTrue(taskId));
        taskExisting.setTaskName(task.getTaskName());
        taskExisting.setDescription(task.getDescription());
        Lane lane = laneServiceImpl.validateLaneById(task.getLane().getLaneId());
        if(Objects.equals(lane.getBoard(), taskExisting.getLane().getBoard())){
            taskExisting.setLane(lane);
            taskRepository.save(taskExisting);
            return taskExisting;
        }
        else {
            throw new IllegalArgumentException("You can't choose lane of another BOARD!");
        }
    }
    @Override
    @Transactional
    public void deleteTaskById(Long taskId) {
        Task task = validateTaskById(taskId);
        task.setActive(false);
        task.getAttachments().forEach(attachment ->
                attachmentRepository.delete(attachment));
        taskRepository.save(task);
        taskUserRepository.deleteByTask(task);
    }
    public Task validateTaskById(Long id) {
        Task existingId = modelMapperTask.mapProjectionToEntity(taskRepository.findTaskByTaskIdAndActiveTrue(id));
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
    private List<Task> sortTaskList(List<Task> tasks){
        List<Task> sortedTasks = new ArrayList<>(tasks);
        sortedTasks.sort(Comparator.comparing(Task::getTaskName)
                .thenComparing(Task::getCreatedDate));
        return sortedTasks;
    }
}
