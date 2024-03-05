package com.fincons.taskmanager.controller;

import com.fincons.taskmanager.dto.TaskDTO;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.mapper.TaskMapper;
import com.fincons.taskmanager.service.taskService.TaskService;
import com.fincons.taskmanager.utility.GenericResponse;
import com.fincons.taskmanager.utility.ValidateFields;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/task-manager")
public class TaskController {

    @Autowired
    private TaskService taskService;
    @Autowired
    private TaskMapper modelMapperTask;

    @GetMapping(value = "${task.find-by-code}")
    public ResponseEntity<GenericResponse<TaskDTO>> getTaskByCode(@RequestParam String code) {
        try {
            ValidateFields.validateSingleField(code);
            Task task = taskService.getTaskByCode(code);
            TaskDTO taskDTO = modelMapperTask.mapToDTO(task);
            GenericResponse<TaskDTO> response = GenericResponse.success(
                    taskDTO,
                    "Success: Found Task with CODE " + code + ".",
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.error(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        } catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rnfe.getMessage(),
                            HttpStatus.NOT_FOUND
                    )
            );
        }
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
        try {
            validateTaskFields(taskDTO);

            Task taskMapped = modelMapperTask.mapToEntity(taskDTO);

            Task task = taskService.createTask(taskMapped);

            TaskDTO taskDTO2 = modelMapperTask.mapToDTO(task);

            GenericResponse<TaskDTO> response = GenericResponse.success(
                    taskDTO2,
                    "Success: Task with code: " + task.getTaskCode() + " has been successfully updated!",
                    HttpStatus.OK);
            return ResponseEntity.ok(response);

        }
        catch (ResourceNotFoundException rfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rfe.getMessage(),
                            HttpStatus.NOT_FOUND
                    )
            );
        }
        catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.error(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        } catch (DuplicateException dne) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    GenericResponse.error(
                            dne.getMessage(),
                            HttpStatus.CONFLICT
                    )
            );
        }
    }
    @PutMapping(value = "${task.put}")
    public ResponseEntity<GenericResponse<TaskDTO>> updateTaskByCode(@RequestParam String taskCode, @RequestBody TaskDTO taskDTO) {
        try {
            ValidateFields.validateSingleField(taskCode);

            validateTaskFields(taskDTO);

            Task taskMapped = modelMapperTask.mapToEntity(taskDTO);

            Task task = taskService.updateTaskByCode(taskCode, taskMapped);

            TaskDTO taskDTO2 = modelMapperTask.mapToDTO(task);

            GenericResponse<TaskDTO> response = GenericResponse.success(
                    taskDTO2,
                    "Success: Task with code: " + taskCode + " has been successfully updated!",
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException rfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rfe.getMessage(),
                            HttpStatus.NOT_FOUND
                    )
            );
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.error(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        } catch (DuplicateException dne) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    GenericResponse.error(
                            dne.getMessage(),
                            HttpStatus.CONFLICT
                    )
            );
        }
    }
    @PutMapping(value = "${task.delete}")
    public ResponseEntity<GenericResponse<TaskDTO>> deleteTaskByCode(@RequestParam String taskCode) {
        try {
            ValidateFields.validateSingleField(taskCode);
            taskService.deleteTaskByCode(taskCode);
            GenericResponse<TaskDTO> response = GenericResponse.empty(
                    "Success: Task with code: " + taskCode + " has been successfully deleted! ",
                    HttpStatus.OK
            );

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.error(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        } catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rnfe.getMessage(),
                            HttpStatus.NOT_FOUND));
        }
    }
    public void validateTaskFields(TaskDTO taskDTO) {
        if (Strings.isEmpty(taskDTO.getTaskCode()) ||
                Strings.isEmpty(taskDTO.getTaskName()) ||
                Strings.isEmpty(taskDTO.getStatus()) ||
                Strings.isEmpty(taskDTO.getBoardCode())) {
            throw new IllegalArgumentException("Error: The fields of the task can't be null or empty.");
        }
    }
}


