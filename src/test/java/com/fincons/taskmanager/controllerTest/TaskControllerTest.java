package com.fincons.taskmanager.controllerTest;

import com.fincons.taskmanager.controller.TaskController;
import com.fincons.taskmanager.dto.TaskDTO;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.mapper.TaskMapper;
import com.fincons.taskmanager.repository.TaskRepository;
import com.fincons.taskmanager.utility.GenericResponse;
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

import static com.fincons.taskmanager.utilityTest.TaskBuilder.getTask;
import static com.fincons.taskmanager.utilityTest.TaskBuilder.getTasks;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
public class TaskControllerTest {

    @Autowired
    private TaskController taskController;
    @MockBean
    private TaskRepository taskRepository;
    @Autowired
    private TaskMapper modelMapperTask;


    @Test
    public void testGetTaskByCodeSuccess() {

        Task task = getTask();
        String taskCode = "code1";
        when(taskRepository.findTaskByTaskCode(taskCode)).thenReturn(task);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.getTaskByCode(taskCode);

        TaskDTO taskDTO = modelMapperTask.mapToDTO(task);

        //Assert if the mapper was successful
        assertThat(taskDTO.getTaskCode()).isEqualTo(task.getTaskCode());
        assertThat(taskDTO.getTaskName()).isEqualTo(task.getTaskName());
        assertThat(taskDTO.getBoardCode()).isEqualTo(task.getBoard().getBoardCode());
        assertThat(taskDTO.getDescription()).isEqualTo(task.getDescription());
        assertThat(taskDTO.getStatus()).isEqualTo(task.getStatus());

        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getBody().getStatus());
    }

    @Test
    void testGetTaskByCodeFailedBadRequest() {
        String taskCodeEmpty = "";
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.getTaskByCode(taskCodeEmpty);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());

        String taskCodeNull = null;
        ResponseEntity<GenericResponse<TaskDTO>> response2 = taskController.getTaskByCode(taskCodeNull);
        Assertions.assertNotNull(response2.getBody());
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getBody().getStatus());
    }
    @Test
    public void testGetTaskByCodeFailedNotFound() {
        String taskCode = "code1";
        when(taskRepository.findTaskByTaskCode(taskCode)).thenReturn(null);
        ResponseEntity<GenericResponse<TaskDTO>> response = taskController.getTaskByCode(taskCode);
        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getBody().getStatus());
    }
    @Test
    public void testGetAllTasksSuccess(){
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
}
