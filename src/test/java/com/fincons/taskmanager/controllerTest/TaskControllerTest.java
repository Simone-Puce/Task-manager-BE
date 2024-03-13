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
    public void testGetTaskById_Success() {

        Task task = getTask();
        Long taskId = 1L;
        when(taskRepository.findTaskByTaskId(taskId)).thenReturn(task);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.getTaskById(taskId);

        assertThat(response.getBody().getData().getTaskId()).isEqualTo(task.getTaskId());
        assertThat(response.getBody().getData().getTaskName()).isEqualTo(task.getTaskName());
        assertThat(response.getBody().getData().getBoardId()).isEqualTo(task.getBoard().getBoardId());
        assertThat(response.getBody().getData().getDescription()).isEqualTo(task.getDescription());
        assertThat(response.getBody().getData().getStatus()).isEqualTo(task.getStatus());

        Assertions.assertEquals(HttpStatus.OK, response.getBody().getStatus());
    }

    @Test
    void testGetTaskById_Failed_BadRequest() {
        Long taskIdEmpty = 0L;
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.getTaskById(taskIdEmpty);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());
    }
    @Test
    void testGetTaskById_Failed_assertThrow_BadRequest(){
        Long taskIdEmpty = 0L;
        taskController.getTaskById(taskIdEmpty);
        assertThrows(IllegalArgumentException.class, () -> {
            ValidateFields.validateSingleFieldLong(taskIdEmpty);
        });
    }
    @Test
    public void testGetTaskById_Failed_NotFound() {
        Long taskId = 1L;
        when(taskRepository.findTaskByTaskId(taskId)).thenReturn(null);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.getTaskById(taskId);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getBody().getStatus());
    }
    @Test
    void testGetTaskById_Failed_assertThrow_NotFound(){
        Long taskId = 1L;
        when(taskRepository.findTaskByTaskId(taskId)).thenReturn(null);
        taskController.getTaskById(taskId);
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.getTaskById(taskId);
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
            assertThat(getTasks().get(i).getTaskId()).isEqualTo(taskDTOs.get(i).getTaskId());
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
        when(taskRepository.findTaskByTaskId(taskDTO.getTaskId())).thenReturn(task);
        when(boardRepository.findBoardByBoardIdAndActiveTrue(taskDTO.getBoardId())).thenReturn(board);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.createTask(taskDTO);

        assertThat(response.getBody().getData().getTaskId()).isEqualTo(task.getTaskId());
        assertThat(response.getBody().getData().getTaskName()).isEqualTo(task.getTaskName());
        assertThat(response.getBody().getData().getBoardId()).isEqualTo(task.getBoard().getBoardId());
        assertThat(response.getBody().getData().getDescription()).isEqualTo(task.getDescription());
        Assertions.assertNotNull(response.getBody().getData());
        Assertions.assertEquals(HttpStatus.OK, response.getBody().getStatus());
    }
    @Test
    public void testCreateTask_Failed_when_findBoardByBoardIdAndActiveTrue_then_NotFoundException(){
        TaskDTO taskDTO = getTaskDTO();
        when(taskRepository.findTaskByTaskId(taskDTO.getTaskId())).thenReturn(null);
        when(boardRepository.findBoardByBoardIdAndActiveTrue(taskDTO.getBoardId())).thenReturn(null);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.createTask(taskDTO);

        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getBody().getStatus());
    }
    @Test
    public void testCreateTask_Failed_assertThrow_NotFoundException(){
        TaskDTO taskDTO = getTaskDTO();
        when(taskRepository.findTaskByTaskId(taskDTO.getTaskId())).thenReturn(null);
        when(boardRepository.findBoardByBoardIdAndActiveTrue(taskDTO.getBoardId())).thenReturn(null);
        Task task = getTask();
        taskController.createTask(taskDTO);
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.createTask(task);
        });
    }
    @Test
    public void testCreateTask_Failed_BadRequest(){
        TaskDTO taskDTO = getTaskDTOWithoutFieldId();
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.createTask(taskDTO);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());
    }
    @Test
    public void testCreateTask_Failed_assertThrow_BadRequest(){
        TaskDTO taskDTO = getTaskDTOWithoutFieldId();
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
        when(taskRepository.existsByTaskIdAndActiveTrue(taskDTO.getTaskId())).thenReturn(true);
        when(boardRepository.findBoardByBoardIdAndActiveTrue(taskDTO.getBoardId())).thenReturn(board);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.createTask(taskDTO);

        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getBody().getStatus());
    }
    @Test
    public void testCreateTask_Failed_asserThrow_Conflict(){
        Board board = getBoard();
        TaskDTO taskDTO = getTaskDTO();
        Task task = getTask();
        when(taskRepository.existsByTaskIdAndActiveTrue(taskDTO.getTaskId())).thenReturn(true);
        when(boardRepository.findBoardByBoardIdAndActiveTrue(taskDTO.getBoardId())).thenReturn(board);
        taskController.createTask(taskDTO);
    }
    @Test
    public void testUpdateTask_Success(){

        Board board = getBoard();
        TaskDTO taskDTO = getTaskDTO();
        Task task = getTask();
        Task taskPut = getTaskForModify();
        TaskDTO taskDTOPut = getTaskDTOForModify();
        when(taskRepository.findTaskByTaskId(taskDTO.getTaskId())).thenReturn(task);
        when(taskRepository.existsByTaskIdAndActiveTrue(taskDTO.getTaskId())).thenReturn(false);
        when(boardRepository.findBoardByBoardIdAndActiveTrue(taskDTOPut.getBoardId())).thenReturn(board);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.updateTaskById(taskDTO.getTaskId(), taskDTOPut);

        assertThat(response.getBody().getData().getTaskId()).isEqualTo(taskPut.getTaskId());
        assertThat(response.getBody().getData().getTaskName()).isEqualTo(taskPut.getTaskName());
        assertThat(response.getBody().getData().getBoardId()).isEqualTo(taskPut.getBoard().getBoardId());
        assertThat(response.getBody().getData().getDescription()).isEqualTo(taskPut.getDescription());
        Assertions.assertNotNull(response.getBody().getData());
        Assertions.assertEquals(HttpStatus.OK, response.getBody().getStatus());
    }
    @Test
    public void testUpdateTask_Failed_NotFoundException(){
        Board board = getBoard();
        TaskDTO taskDTO = getTaskDTO();
        Task task = getTask();
        when(taskRepository.findTaskByTaskId(taskDTO.getTaskId())).thenReturn(null);
        when(boardRepository.findBoardByBoardIdAndActiveTrue(taskDTO.getBoardId())).thenReturn(board);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.updateTaskById(taskDTO.getTaskId(), taskDTO);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getBody().getStatus());
    }
    @Test
    public void testUpdateTask_Failed_assertThrow_NotFoundException(){
        Board board = getBoard();
        TaskDTO taskDTO = getTaskDTO();
        Task task = getTask();
        when(taskRepository.findTaskByTaskId(taskDTO.getTaskId())).thenReturn(null);
        when(boardRepository.findBoardByBoardIdAndActiveTrue(taskDTO.getBoardId())).thenReturn(board);
        taskController.updateTaskById(taskDTO.getTaskId(), taskDTO);
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.updateTaskById(taskDTO.getTaskId(), task);
        });
    }
    @Test
    public void testUpdateTask_Failed_BadRequest(){
        TaskDTO taskDTO = getTaskDTOWithoutOtherFieldCannotBeNull();
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.updateTaskById(taskDTO.getTaskId(), taskDTO);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());
    }
    @Test
    public void testUpdateTask_Failed_assertThrow_BadRequest(){
        TaskDTO taskDTO = getTaskDTOWithoutFieldId();
        TaskDTO taskDTO1 = getTaskDTOWithoutOtherFieldCannotBeNull();
        taskController.updateTaskById(taskDTO.getTaskId(), taskDTO);
        assertThrows(IllegalArgumentException.class, () -> {
            ValidateFields.validateSingleFieldLong(taskDTO.getTaskId());
        });
        taskController.updateTaskById(taskDTO1.getTaskId(), taskDTO1);
        assertThrows(IllegalArgumentException.class, () -> {
            taskController.validateTaskFields(taskDTO);
        });
    }
    @Test
    public void testUpdateTask_Failed_Conflict(){
        TaskDTO taskDTO = getTaskDTO();
        Task task = getTask();
        when(taskRepository.findTaskByTaskId(taskDTO.getTaskId())).thenReturn(task);
        when(taskRepository.existsByTaskIdAndActiveTrue(taskDTO.getTaskId())).thenReturn(true);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.updateTaskById(taskDTO.getTaskId(), taskDTO);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.CONFLICT, response.getBody().getStatus());
    }
    @Test
    public void testUpdateTask_Failed_assertThrow_Conflict(){
        TaskDTO taskDTO = getTaskDTO();
        Task task = getTask();
        Board board = getBoard();
        when(taskRepository.findTaskByTaskId(taskDTO.getTaskId())).thenReturn(task);
        when(boardRepository.findBoardByBoardIdAndActiveTrue(taskDTO.getBoardId())).thenReturn(board);
        when(taskRepository.existsByTaskIdAndActiveTrue(taskDTO.getTaskId())).thenReturn(true);
        taskController.updateTaskById(taskDTO.getTaskId(), taskDTO);
    }
    @Test
    void testDeleteTask_Success(){
        Task task = getTask();
        Long taskId = 1L;
        when(taskRepository.findTaskByTaskId(taskId)).thenReturn(task);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.deleteTaskById(taskId);
        assertThat(response.getBody()).toString().isEmpty();
        Assertions.assertEquals(HttpStatus.OK, response.getBody().getStatus());
    }
    @Test
    void testDeleteTask_Failed_BadRequest(){
        Task task = getTask();
        Long taskId = 0L;
        when(taskRepository.findTaskByTaskId(taskId)).thenReturn(task);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.deleteTaskById(taskId);
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());
    }
    @Test
    void testDeleteTask_Failed_assertThrow_BadRequest(){
        Task task = getTask();
        Long taskId = 0L;
        when(taskRepository.findTaskByTaskId(taskId)).thenReturn(task);
        taskController.deleteTaskById(taskId);
        assertThrows(IllegalArgumentException.class, () -> {
            ValidateFields.validateSingleFieldLong(taskId);
        });
    }
    @Test
    void testDeleteTask_Failed_NotFound(){
        Task task = getTask();
        Long taskId = 1L;
        when(taskRepository.findTaskByTaskId(taskId)).thenReturn(null);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.deleteTaskById(taskId);
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getBody().getStatus());
    }
    @Test
    void testDeleteTask_Failed_assertThrow_NotFound(){
        Task task = getTask();
        Long taskId = 1L;
        when(taskRepository.findTaskByTaskId(taskId)).thenReturn(null);
        taskController.deleteTaskById(taskId);
        assertThrows(ResourceNotFoundException.class, () -> {
            taskService.deleteTaskById(taskId);
        });
    }
}
