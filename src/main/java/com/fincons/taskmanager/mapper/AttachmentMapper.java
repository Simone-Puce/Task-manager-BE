package com.fincons.taskmanager.mapper;


import com.fincons.taskmanager.dto.AttachmentDTO;
import com.fincons.taskmanager.entity.Attachment;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AttachmentMapper {

    @Autowired
    private ModelMapper modelMapperStandard;

    public AttachmentDTO mapToDTO(Attachment attachment) {
        return modelMapperStandard.map(attachment, AttachmentDTO.class);
    }
    public List<AttachmentDTO> mapEntitiesToDTOs(List<Attachment> attachments){
        return attachments.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public Attachment mapToEntity(AttachmentDTO attachmentDTO){
        return modelMapperStandard.map(attachmentDTO, Attachment.class);
    }
}
