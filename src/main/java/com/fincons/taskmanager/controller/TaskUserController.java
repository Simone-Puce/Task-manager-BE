package com.fincons.taskmanager.controller;


import com.fincons.taskmanager.dto.TaskUserDTO;
import com.fincons.taskmanager.entity.TaskUser;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.mapper.TaskUserMapper;
import com.fincons.taskmanager.service.taskUserService.TaskUserService;
import com.fincons.taskmanager.utility.GenericResponse;
import com.fincons.taskmanager.utility.ValidateFields;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/task-manager")
public class TaskUserController {
    @Autowired
    private TaskUserService taskUserService;
    
    @Autowired
    private TaskUserMapper modelMapperTaskUser;

    @GetMapping(value = "${task.user.find.tasks.by.user}")
    public ResponseEntity<GenericResponse<List<TaskUserDTO>>> findTasksByUser(@RequestParam String email){
        try{
            ValidateFields.validateSingleField(email);
            List<TaskUser> tasksUsers = taskUserService.findTasksByUser(email);
            List<TaskUserDTO> userBoardDTOs = modelMapperTaskUser.mapEntitiesToDTOs(tasksUsers);

            GenericResponse<List<TaskUserDTO>> response = GenericResponse.success(
                    userBoardDTOs,
                    "Success: There are all TASKS for the User with email : " + email,
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        }
        catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.empty(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        }
        catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rnfe.getMessage(),
                            HttpStatus.NOT_FOUND));
        }
        catch (DuplicateException dne) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    GenericResponse.error(
                            dne.getMessage(),
                            HttpStatus.CONFLICT
                    )
            );
        }
    }
    @PostMapping(value = "${task.user.create}")
    public ResponseEntity<GenericResponse<TaskUserDTO>> createTaskUser(@RequestBody TaskUserDTO taskUserDTO){
        try{
            validateTaskUserFields(taskUserDTO);
            TaskUser taskUserMapped = modelMapperTaskUser.mapToEntity(taskUserDTO);
            TaskUser taskUser = taskUserService.createTaskUser(taskUserMapped);

            TaskUserDTO taskUserDTO2 = modelMapperTaskUser.mapToDTO(taskUser);
            GenericResponse<TaskUserDTO> response = GenericResponse.success(
                    taskUserDTO2,
                    "Success: Addition of relationship between Task with code: " + taskUserDTO2.getTaskCode() + " and USER with email: " + taskUserDTO2.getEmail(),
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        }
        catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.empty(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        }
        catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rnfe.getMessage(),
                            HttpStatus.NOT_FOUND));
        }
        catch (DuplicateException dne) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    GenericResponse.error(
                            dne.getMessage(),
                            HttpStatus.CONFLICT
                    )
            );
        }
    }
    @PutMapping(value = "${task.user.put}")
    public ResponseEntity<GenericResponse<TaskUserDTO>> updateTaskUser(@RequestParam String taskCode,
                                                                         @RequestParam String email,
                                                                         @RequestBody TaskUserDTO taskUserDTO){
        try {
            ValidateFields.validateSingleField(taskCode);
            ValidateFields.validateSingleField(email);
            validateTaskUserFields(taskUserDTO);
            TaskUser taskUserMapped = modelMapperTaskUser.mapToEntity(taskUserDTO);
            TaskUser taskUser = taskUserService.updateTaskUser(taskCode, email, taskUserMapped);
            TaskUserDTO taskUserDTO2 = modelMapperTaskUser.mapToDTO(taskUser);
            GenericResponse<TaskUserDTO> response = GenericResponse.success(
                    taskUserDTO2,
                    "Success: Addition of relationship between task with CODE: " + taskCode + " and user with EMAIL: " + email,
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        }
        catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.empty(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        }
        catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rnfe.getMessage(),
                            HttpStatus.NOT_FOUND));
        }
        catch (DuplicateException dne) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    GenericResponse.error(
                            dne.getMessage(),
                            HttpStatus.CONFLICT
                    )
            );
        }
    }
    @DeleteMapping(value = "${task.user.delete}")
    public ResponseEntity<GenericResponse<TaskUserDTO>> deleteTaskUser(@RequestParam String taskCode, @RequestParam String email) {
        try {
            ValidateFields.validateSingleField(taskCode);
            ValidateFields.validateSingleField(email);
            TaskUser taskUser = taskUserService.deleteTaskUser(taskCode, email);
            TaskUserDTO taskUserDTO = modelMapperTaskUser.mapToDTO(taskUser);
            GenericResponse<TaskUserDTO> response = GenericResponse.success(
                    taskUserDTO,
                    "Success: Delete relationship between task with CODE: " + taskCode + " and user with EMAIL: " + email,
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        }
        catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.empty(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        }
        catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rnfe.getMessage(),
                            HttpStatus.NOT_FOUND));
        }
        catch (DuplicateException dne) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    GenericResponse.error(
                            dne.getMessage(),
                            HttpStatus.CONFLICT
                    )
            );
        }
    }
    private void validateTaskUserFields(TaskUserDTO taskUserDTO) {
        if (Strings.isEmpty(taskUserDTO.getTaskCode()) ||
                Strings.isEmpty(taskUserDTO.getEmail()))  {
            throw new IllegalArgumentException("Error: The fields of the task-user can't be null or empty.");
        }
    }
}