package com.fincons.taskmanager.mapper;

import com.fincons.taskmanager.dto.AttachmentDTO;
import com.fincons.taskmanager.dto.TaskDTO;
import com.fincons.taskmanager.entity.Attachment;
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
    public TaskDTO mapToDTOOnlyActive(Task task) {
        TaskDTO taskDTO = modelMapperForTask.map(task, TaskDTO.class);
        List<AttachmentDTO> activeAttachments = task.getAttachments().stream()
                .filter(Attachment::isActive)
                .map(attachment -> modelMapperForTask.map(attachment, AttachmentDTO.class))
                .collect(Collectors.toList());
        taskDTO.setAttachments(activeAttachments);
        return taskDTO;
    }
    public TaskDTO mapToDTOOnlyInactive(Task task) {
        TaskDTO taskDTO = modelMapperForTask.map(task, TaskDTO.class);
        List<AttachmentDTO> activeAttachments = task.getAttachments().stream()
                .filter(attachment -> !attachment.isActive())
                .map(attachment -> modelMapperForTask.map(attachment, AttachmentDTO.class))
                .collect(Collectors.toList());
        taskDTO.setAttachments(activeAttachments);
        return taskDTO;
    }
    public List<TaskDTO> mapEntitiesToDTOsOnlyActive(List<Task> tasks) {
        return tasks.stream()
                .map(task -> {
                    TaskDTO taskDTO = modelMapperForTask.map(task, TaskDTO.class);
                    List<AttachmentDTO> filteredAttachments = task.getAttachments().stream()
                            .filter(Attachment::isActive)
                            .map(attachment -> modelMapperForTask.map(attachment, AttachmentDTO.class))
                            .collect(Collectors.toList());
                    taskDTO.setAttachments(filteredAttachments);
                    return taskDTO;
                })
                .collect(Collectors.toList());
    }
}

