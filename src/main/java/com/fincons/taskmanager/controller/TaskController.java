package com.fincons.taskmanager.controller;

import com.fincons.taskmanager.dto.TaskDTO;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.mapper.TaskMapper;
import com.fincons.taskmanager.service.taskService.TaskService;
import com.fincons.taskmanager.utility.GenericResponse;
import com.fincons.taskmanager.utility.MaxCharLength;
import com.fincons.taskmanager.utility.SpaceAndFormatValidator;
import com.fincons.taskmanager.utility.ValidateFields;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ControllerAdvice
@CrossOrigin("*")
@RequestMapping("/task-manager")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskMapper modelMapperTask;

    @GetMapping(value = "${task.find-by-id}")
    public ResponseEntity<GenericResponse<TaskDTO>> getTaskById(@RequestParam Long id) {
        ValidateFields.validateSingleFieldLong(id);
        Task task = taskService.getTaskById(id);
        TaskDTO taskDTO = modelMapperTask.mapToDTO(task);
        GenericResponse<TaskDTO> response = GenericResponse.success(
                taskDTO,
                "Success: Found Task with CODE " + id + ".",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }
    @GetMapping(value = "${task.list}")
    public ResponseEntity<GenericResponse<List<TaskDTO>>> getAllTasks() {
        List<Task> tasks = taskService.getAllTasks();
        List<TaskDTO> taskDTOs = modelMapperTask.mapEntitiesToDTOs(tasks);
        GenericResponse<List<TaskDTO>> response = GenericResponse.success(
                taskDTOs,
                "Success:" + (taskDTOs.isEmpty() || taskDTOs.size() == 1 ? " Found " : " Founds ") + taskDTOs.size() +
                        (taskDTOs.isEmpty() || taskDTOs.size() == 1 ? " task" : " tasks") + ".",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping(value = "${task.create}")
    public ResponseEntity<GenericResponse<TaskDTO>> createTask(@RequestBody TaskDTO taskDTO) {
        validateTaskDTO(taskDTO);
        Task taskMapped = modelMapperTask.mapToEntity(taskDTO);
        Task task = taskService.createTask(taskMapped);
        TaskDTO taskDTO2 = modelMapperTask.mapToDTO(task);
        GenericResponse<TaskDTO> response = GenericResponse.success(
                taskDTO2,
                "Success: Task with id: " + task.getTaskId() + " has been successfully updated!",
                HttpStatus.OK);
        return ResponseEntity.ok(response);
    }
    @PutMapping(value = "${task.put}")
    public ResponseEntity<GenericResponse<TaskDTO>> updateTaskById(@RequestParam Long taskId, @RequestBody TaskDTO taskDTO) {
        ValidateFields.validateSingleFieldLong(taskId);
        validateTaskDTO(taskDTO);
        Task taskMapped = modelMapperTask.mapToEntity(taskDTO);
        Task task = taskService.updateTaskById(taskId, taskMapped);
        TaskDTO taskDTO2 = modelMapperTask.mapToDTO(task);
        GenericResponse<TaskDTO> response = GenericResponse.success(
                taskDTO2,
                "Success: Task with id: " + taskId + " has been successfully updated!",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }
    @PutMapping(value = "${task.delete}")
    public ResponseEntity<GenericResponse<TaskDTO>> deleteTaskById(@RequestParam Long taskId) {

        ValidateFields.validateSingleFieldLong(taskId);
        taskService.deleteTaskById(taskId);
        GenericResponse<TaskDTO> response = GenericResponse.empty(
                "Success: Task with id: " + taskId + " has been successfully deleted! ",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }
    private void validateTaskDTO(TaskDTO taskDTO) {
        validateTaskFields(taskDTO);
        String newTaskName = SpaceAndFormatValidator.spaceAndFormatValidator(taskDTO.getTaskName());
        MaxCharLength.validateNameLength(newTaskName);
        taskDTO.setTaskName(newTaskName);
        if (!Strings.isEmpty(taskDTO.getDescription())) {
            String newDescription = SpaceAndFormatValidator.spaceAndFormatValidator(taskDTO.getDescription());
            MaxCharLength.validateDescriptionLength(newTaskName);
            taskDTO.setDescription(newDescription);
        }
    }
    public void validateTaskFields(TaskDTO taskDTO) {
        if (Strings.isEmpty(taskDTO.getTaskName())) {
            throw new IllegalArgumentException("Error: The fields of the task can't be null or empty.");
        }
    }
}


