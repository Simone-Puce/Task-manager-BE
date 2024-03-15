package com.fincons.taskmanager.service.taskService.impl;

import com.fincons.taskmanager.entity.Attachment;
import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.Lane;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.TaskRepository;
import com.fincons.taskmanager.repository.TaskUserRepository;
import com.fincons.taskmanager.service.boardService.impl.BoardServiceImpl;
import com.fincons.taskmanager.service.laneService.LaneService;
import com.fincons.taskmanager.service.laneService.impl.LaneServiceImpl;
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
    private LaneServiceImpl laneServiceImpl;
    @Autowired
    private TaskUserRepository taskUserRepository;

    @Override
    public Task getTaskById(Long taskId) {
        existingTaskById(taskId);
        Task taskForAttachments = taskRepository.findTaskIdForAttachmentsAllTrue(taskId);
        if(Objects.isNull(taskForAttachments)){
            Task taskForAttachmentsNonNull = taskRepository.findTaskByTaskIdAndActiveTrue(taskId);
            return filterTaskForAttachmentsTrue(taskForAttachmentsNonNull);
        }
        return taskForAttachments;
    }

    @Override
    public List<Task> getAllTasks() {
        List<Task> tasks = taskRepository.findAllForAttachmentsAllTrue();
        if (Objects.isNull(tasks)){
            List<Task> tasksForAttachments = taskRepository.findAllByActiveTrue();
            return filterTasksForAttachmentsTrue(tasksForAttachments);
        }
        return tasks;
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
        Task taskExisting = taskRepository.findTaskIdForAttachmentsAllTrue(taskId);
        taskExisting.setTaskName(task.getTaskName());
        taskExisting.setDescription(task.getDescription());
        Lane lane = laneServiceImpl.validateLaneById(task.getLane().getLaneId());
        taskExisting.setLane(lane);
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
    private Task filterTaskForAttachmentsTrue(Task task) {
        List<Attachment> attachments = task.getAttachments().stream()
                .filter(Attachment::isActive)
                .toList();
        task.setAttachments(attachments);
        return task;
    }
    private List<Task> filterTasksForAttachmentsTrue(List<Task> tasks){
        return tasks.stream()
                .map(this::filterTaskForAttachmentsTrue)
                .toList();
    }
}
