package com.fincons.taskmanager.controllerTest;

import com.fincons.taskmanager.controller.TaskController;
import com.fincons.taskmanager.dto.TaskDTO;
import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.mapper.TaskMapper;
import com.fincons.taskmanager.repository.BoardRepository;
import com.fincons.taskmanager.repository.TaskRepository;
import com.fincons.taskmanager.service.taskService.TaskService;
import com.fincons.taskmanager.service.taskService.impl.TaskServiceImpl;
import com.fincons.taskmanager.utility.GenericResponse;
import com.fincons.taskmanager.utility.ValidateFields;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static com.fincons.taskmanager.utilityTest.boardBuilder.BoardBuilder.getBoard;
import static com.fincons.taskmanager.utilityTest.taskBuilder.TaskBuilder.*;
import static com.fincons.taskmanager.utilityTest.taskBuilder.TaskDTOBuilder.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class TaskControllerTest {

    @Autowired
    private TaskController taskController;
    @MockBean
    private TaskRepository taskRepository;
    @MockBean
    private BoardRepository boardRepository;
    @Autowired
    private TaskService taskService;


    @Test
    public void testGetTaskByCode_Success() {

        Task task = getTask();
        String taskCode = "taskCode1";
        when(taskRepository.findTaskByTaskCode(taskCode)).thenReturn(task);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.getTaskByCode(taskCode);

        assertThat(task.getId()).isNotNull();
        assertThat(response.getBody().getData().getTaskCode()).isEqualTo(task.getTaskCode());
        assertThat(response.getBody().getData().getTaskName()).isEqualTo(task.getTaskName());
        assertThat(response.getBody().getData().getBoardCode()).isEqualTo(task.getBoard().getBoardCode());
        assertThat(response.getBody().getData().getDescription()).isEqualTo(task.getDescription());
        assertThat(response.getBody().getData().getStatus()).isEqualTo(task.getStatus());

        Assertions.assertEquals(HttpStatus.OK, response.getBody().getStatus());
    }

    @Test
    void testGetTaskByCode_Failed_BadRequest() {
        String taskCodeEmpty = "";
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.getTaskByCode(taskCodeEmpty);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());
    }
    @Test
    void testGetTaskByCode_Failed_assertThrow_BadRequest(){
        String taskCodeEmpty = "";
        taskController.getTaskByCode(taskCodeEmpty);
        assertThrows(IllegalArgumentException.class, () -> {
            ValidateFields.validateSingleField(taskCodeEmpty);
        });
    }
    @Test
    public void testGetTaskByCode_Failed_NotFound() {
        String taskCode = "code1";
        when(taskRepository.findTaskByTaskCode(taskCode)).thenReturn(null);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.getTaskByCode(taskCode);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getBody().getStatus());
    }
    @Test
    void testGetTaskByCode_Failed_assertThrow_NotFound(){
        String taskCode = "code1";
        when(taskRepository.findTaskByTaskCode(taskCode)).thenReturn(null);
        taskController.getTaskByCode(taskCode);
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.getTaskByCode(taskCode);
        });
    }
    @Test
    public void testGetAllTasks_Success(){
        when(taskRepository.findAll()).thenReturn(getTasks());
        ResponseEntity<GenericResponse<List<TaskDTO>>> response = taskController.getAllTasks();

        List<TaskDTO> taskDTOs = response.getBody().getData().stream().toList();

        assertThat(getTasks().size()).isEqualTo(taskDTOs.size());

        int iterations = 0;
        for (int i = 0; i < taskDTOs.size(); i++) {
            assertThat(getTasks().get(i).getTaskCode()).isEqualTo(taskDTOs.get(i).getTaskCode());
            assertThat(getTasks().get(i).getTaskName()).isEqualTo(taskDTOs.get(i).getTaskName());
            assertThat(getTasks().get(i).getDescription()).isEqualTo(taskDTOs.get(i).getDescription());
            assertThat(getTasks().get(i).getStatus()).isEqualTo(taskDTOs.get(i).getStatus());
            iterations++;
        }
        assertThat(iterations).isEqualTo(taskDTOs.size());
        assertThat(iterations).isEqualTo(getTasks().size());
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getBody().getStatus());
    }
    @Test
    public void testCreateTask_Success(){
        Task task = getTask();
        TaskDTO taskDTO = getTaskDTO();
        Board board = getBoard();
        when(taskRepository.findTaskByTaskCode(taskDTO.getTaskCode())).thenReturn(task);
        when(boardRepository.findBoardByBoardCode(taskDTO.getBoardCode())).thenReturn(board);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.createTask(taskDTO);

        assertThat(task.getId()).isNotNull();
        assertThat(response.getBody().getData().getTaskCode()).isEqualTo(task.getTaskCode());
        assertThat(response.getBody().getData().getTaskName()).isEqualTo(task.getTaskName());
        assertThat(response.getBody().getData().getBoardCode()).isEqualTo(task.getBoard().getBoardCode());
        assertThat(response.getBody().getData().getDescription()).isEqualTo(task.getDescription());
        Assertions.assertNotNull(response.getBody().getData());
        Assertions.assertEquals(HttpStatus.OK, response.getBody().getStatus());
    }
    @Test
    public void testCreateTask_Failed_when_findBoardByBoardCode_then_NotFoundException(){
        TaskDTO taskDTO = getTaskDTO();
        when(taskRepository.findTaskByTaskCode(taskDTO.getTaskCode())).thenReturn(null);
        when(boardRepository.findBoardByBoardCode(taskDTO.getBoardCode())).thenReturn(null);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.createTask(taskDTO);

        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getBody().getStatus());
    }
    @Test
    public void testCreateTask_Failed_assertThrow_NotFoundException(){
        TaskDTO taskDTO = getTaskDTO();
        when(taskRepository.findTaskByTaskCode(taskDTO.getTaskCode())).thenReturn(null);
        when(boardRepository.findBoardByBoardCode(taskDTO.getBoardCode())).thenReturn(null);
        Task task = getTask();
        taskController.createTask(taskDTO);
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.createTask(task);
        });
    }
    @Test
    public void testCreateTask_Failed_BadRequest(){
        TaskDTO taskDTO = getTaskDTOWithoutFieldCode();
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.createTask(taskDTO);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());
    }
    @Test
    public void testCreateTask_Failed_assertThrow_BadRequest(){
        TaskDTO taskDTO = getTaskDTOWithoutFieldCode();
        taskController.createTask(taskDTO);
        assertThrows(IllegalArgumentException.class, () -> {
            taskController.validateTaskFields(taskDTO);
        });
    }
    @Test
    public void testCreateTask_Failed_Conflict(){

        Board board = getBoard();
        TaskDTO taskDTO = getTaskDTO();
        Task task = getTask();
        when(taskRepository.existsByTaskCode(taskDTO.getTaskCode())).thenReturn(true);
        when(boardRepository.findBoardByBoardCode(taskDTO.getBoardCode())).thenReturn(board);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.createTask(taskDTO);

        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getBody().getStatus());
    }
    @Test
    public void testCreateTask_Failed_asserThrow_Conflict(){
        Board board = getBoard();
        TaskDTO taskDTO = getTaskDTO();
        Task task = getTask();
        when(taskRepository.existsByTaskCode(taskDTO.getTaskCode())).thenReturn(true);
        when(boardRepository.findBoardByBoardCode(taskDTO.getBoardCode())).thenReturn(board);
        taskController.createTask(taskDTO);
        assertThrows(DuplicateException.class, () -> {
            taskService.validateTaskByCodeAlreadyExist(taskDTO.getTaskCode());
        });
    }
    @Test
    public void testUpdateTask_Success(){

        Board board = getBoard();
        TaskDTO taskDTO = getTaskDTO();
        Task task = getTask();
        Task taskPut = getTaskForModify();
        TaskDTO taskDTOPut = getTaskDTOForModify();
        when(taskRepository.findTaskByTaskCode(taskDTO.getTaskCode())).thenReturn(task);
        when(taskRepository.existsByTaskCode(taskDTO.getTaskCode())).thenReturn(false);
        when(boardRepository.findBoardByBoardCode(taskDTOPut.getBoardCode())).thenReturn(board);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.updateTaskByCode(taskDTO.getTaskCode(), taskDTOPut);

        assertThat(response.getBody().getData().getTaskCode()).isEqualTo(taskPut.getTaskCode());
        assertThat(response.getBody().getData().getTaskName()).isEqualTo(taskPut.getTaskName());
        assertThat(response.getBody().getData().getBoardCode()).isEqualTo(taskPut.getBoard().getBoardCode());
        assertThat(response.getBody().getData().getDescription()).isEqualTo(taskPut.getDescription());
        Assertions.assertNotNull(response.getBody().getData());
        Assertions.assertEquals(HttpStatus.OK, response.getBody().getStatus());
    }
    @Test
    public void testUpdateTask_Failed_NotFoundException(){
        Board board = getBoard();
        TaskDTO taskDTO = getTaskDTO();
        Task task = getTask();
        when(taskRepository.findTaskByTaskCode(taskDTO.getTaskCode())).thenReturn(null);
        when(boardRepository.findBoardByBoardCode(taskDTO.getBoardCode())).thenReturn(board);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.updateTaskByCode(taskDTO.getTaskCode(), taskDTO);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getBody().getStatus());
    }
    @Test
    public void testUpdateTask_Failed_assertThrow_NotFoundException(){
        Board board = getBoard();
        TaskDTO taskDTO = getTaskDTO();
        Task task = getTask();
        when(taskRepository.findTaskByTaskCode(taskDTO.getTaskCode())).thenReturn(null);
        when(boardRepository.findBoardByBoardCode(taskDTO.getBoardCode())).thenReturn(board);
        taskController.updateTaskByCode(taskDTO.getTaskCode(), taskDTO);
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.updateTaskByCode(taskDTO.getTaskCode(), task);
        });
    }
    @Test
    public void testUpdateTask_Failed_BadRequest(){
        TaskDTO taskDTO = getTaskDTOWithoutOtherFieldCannotBeNull();
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.updateTaskByCode(taskDTO.getTaskCode(), taskDTO);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());
    }
    @Test
    public void testUpdateTask_Failed_assertThrow_BadRequest(){
        TaskDTO taskDTO = getTaskDTOWithoutFieldCode();
        TaskDTO taskDTO1 = getTaskDTOWithoutOtherFieldCannotBeNull();
        taskController.updateTaskByCode(taskDTO.getTaskCode(), taskDTO);
        assertThrows(IllegalArgumentException.class, () -> {
            ValidateFields.validateSingleField(taskDTO.getTaskCode());
        });
        taskController.updateTaskByCode(taskDTO1.getTaskCode(), taskDTO1);
        assertThrows(IllegalArgumentException.class, () -> {
            taskController.validateTaskFields(taskDTO);
        });
    }
    @Test
    public void testUpdateTask_Failed_Conflict(){
        TaskDTO taskDTO = getTaskDTO();
        Task task = getTask();
        when(taskRepository.findTaskByTaskCode(taskDTO.getTaskCode())).thenReturn(task);
        when(taskRepository.existsByTaskCode(taskDTO.getTaskCode())).thenReturn(true);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.updateTaskByCode(taskDTO.getTaskCode(), taskDTO);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getBody().getStatus());
    }
    @Test
    public void testUpdateTask_Failed_assertThrow_Conflict(){
        TaskDTO taskDTO = getTaskDTO();
        Task task = getTask();
        Board board = getBoard();
        when(taskRepository.findTaskByTaskCode(taskDTO.getTaskCode())).thenReturn(task);
        when(boardRepository.findBoardByBoardCode(taskDTO.getBoardCode())).thenReturn(board);
        when(taskRepository.existsByTaskCode(taskDTO.getTaskCode())).thenReturn(true);
        taskController.updateTaskByCode(taskDTO.getTaskCode(), taskDTO);
        assertThrows(DuplicateException.class, () -> {
            taskService.validateTaskByCodeAlreadyExist(taskDTO.getTaskCode());
        });
    }
    @Test
    void testDeleteTask_Success(){
        Task task = getTask();
        String taskCode = "taskCode1";
        when(taskRepository.findTaskByTaskCode(taskCode)).thenReturn(task);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.deleteTaskByCode(taskCode);
        assertThat(response.getBody()).toString().isEmpty();
        Assertions.assertEquals(HttpStatus.OK, response.getBody().getStatus());
    }
    @Test
    void testDeleteTask_Failed_BadRequest(){
        Task task = getTask();
        String taskCode = "";
        when(taskRepository.findTaskByTaskCode(taskCode)).thenReturn(task);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.deleteTaskByCode(taskCode);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());
    }
    @Test
    void testDeleteTask_Failed_assertThrow_BadRequest(){
        Task task = getTask();
        String taskCode = "";
        when(taskRepository.findTaskByTaskCode(taskCode)).thenReturn(task);
        taskController.deleteTaskByCode(taskCode);
        assertThrows(IllegalArgumentException.class, () -> {
            ValidateFields.validateSingleField(taskCode);
        });
    }
    @Test
    void testDeleteTask_Failed_NotFound(){
        Task task = getTask();
        String taskCode = "taskCode1";
        when(taskRepository.findTaskByTaskCode(taskCode)).thenReturn(null);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.deleteTaskByCode(taskCode);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getBody().getStatus());
    }
    @Test
    void testDeleteTask_Failed_assertThrow_NotFound(){
        Task task = getTask();
        String taskCode = "taskCode1";
        when(taskRepository.findTaskByTaskCode(taskCode)).thenReturn(null);
        taskController.deleteTaskByCode(taskCode);
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.deleteTaskByCode(taskCode);
        });
    }
}
