package com.fincons.taskmanager.mapper;

import com.fincons.taskmanager.dto.TaskUserDTO;
import com.fincons.taskmanager.entity.TaskUser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TaskUserMapper {
    @Autowired
    private ModelMapper modelMapperForTaskUser;

    public TaskUserDTO mapToDTO(TaskUser taskUser){
        return modelMapperForTaskUser.map(taskUser, TaskUserDTO.class);
    }
    public List<TaskUserDTO> mapEntitiesToDTOs(List<TaskUser> tasksUsers){
        return tasksUsers.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    public TaskUser mapToEntity(TaskUserDTO taskUserDTO){
        return modelMapperForTaskUser.map(taskUserDTO, TaskUser.class);
    }
}
