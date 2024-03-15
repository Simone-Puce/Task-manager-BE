package com.fincons.taskmanager.mapper;


import com.fincons.taskmanager.dto.TaskDTO;
import com.fincons.taskmanager.entity.Task;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskMapper {

    @Autowired
    private ModelMapper modelMapperForTask;

    public TaskDTO mapToDTO(Task task) {
        return modelMapperForTask.map(task, TaskDTO.class);
    }
    public Task mapToEntity(TaskDTO taskDTO){
        return modelMapperForTask.map(taskDTO, Task.class);
    }
    public List<TaskDTO> mapEntitiesToDTOs(List<Task> tasks){
        return tasks.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}

