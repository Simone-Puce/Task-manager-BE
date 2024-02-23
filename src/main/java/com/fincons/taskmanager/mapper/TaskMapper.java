package com.fincons.taskmanager.mapper;

import com.fincons.taskmanager.dto.TaskDTO;
import com.fincons.taskmanager.entity.Task;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {
    

    private ModelMapper modelMapperStandard = new ModelMapper();

    public TaskDTO mapToDTO(Task task) {
        return modelMapperStandard.map(task, TaskDTO.class);
    }

    public Task mapToEntity(TaskDTO taskDTO){
        return modelMapperStandard.map(taskDTO, Task.class);
    }

}

