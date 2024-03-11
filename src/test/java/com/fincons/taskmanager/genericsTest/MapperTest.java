package com.fincons.taskmanager.genericsTest;

import com.fincons.taskmanager.dto.*;
import com.fincons.taskmanager.entity.*;
import org.junit.jupiter.api.Test;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;

import java.util.ArrayList;
import java.util.List;

public class MapperTest {
    private ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        final List<AttachmentDTO> filteredAttachments = new ArrayList<>();
        Condition<List<Attachment>, List<AttachmentDTO>> attachmentNotActive = new Condition<List<Attachment>, List<AttachmentDTO>>() {
            @Override
            public boolean applies(MappingContext<List<Attachment>, List<AttachmentDTO>> context) {
                List<Attachment> attachments = context.getSource();
                if (attachments != null) {
                    for (Attachment attachment : attachments) {
                        if (attachment.isActive()) {
                            filteredAttachments.add(modelMapper.map(attachment, AttachmentDTO.class));
                        }
                    }
                }
                return true;
            }
        };
        PropertyMap<Task, TaskDTO> propertyMapToTaskDTO = new PropertyMap<>() {
            protected void configure() {
                when(attachmentNotActive).skip().setAttachments(filteredAttachments);
            }
        };
        modelMapper.addMappings(propertyMapToTaskDTO);
        return modelMapper;
    }
    @Test
    public void testMapper() {

        AttachmentDTO attachmentDTOTrue = new AttachmentDTO();
    }

}
