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

import static com.fincons.taskmanager.utilityTest.TaskBuilder.getTask;
import static org.assertj.core.api.Assertions.assertThat;
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
        assertThat(taskDTO.getName()).isEqualTo(task.getName());
        assertThat(taskDTO.getBoardCode()).isEqualTo(task.getBoard().getBoardCode());
        assertThat(taskDTO.getDescription()).isEqualTo(task.getDescription());
        assertThat(taskDTO.getStatus()).isEqualTo(task.getStatus());
        assertThat(taskDTO.getUsers().size()).isEqualTo(task.getUsers().size());

        Assertions.assertNotNull(response.getBody());
        Assertions.assertEquals(HttpStatus.OK, response.getBody().getStatus());
    }
}
