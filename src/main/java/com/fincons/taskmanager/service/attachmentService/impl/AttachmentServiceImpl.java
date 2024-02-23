package com.fincons.taskmanager.service.attachmentService.impl;



import com.fincons.taskmanager.dto.AttachmentDTO;
import com.fincons.taskmanager.entity.Attachment;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.AttachmentRepository;
import com.fincons.taskmanager.service.attachmentService.AttachmentService;
import com.fincons.taskmanager.service.taskService.impl.TaskServiceImpl;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AttachmentServiceImpl implements AttachmentService {

    @Autowired
    private AttachmentRepository attachmentRepository;

    @Autowired
    private TaskServiceImpl taskServiceImpl;

    @Override
    public Attachment getAttachmentByCode(String attachmentCode) {
        return validateAttachmentByCode(attachmentCode);
    }

    @Override
    public List<Attachment> getAllAttachments() {
        return attachmentRepository.findAll();
    }

    @Override
    public Attachment createAttachment(Attachment attachment) {
        checkForDuplicateAttachment(attachment.getAttachmentCode());

        Task task = taskServiceImpl.validateTaskByCode(attachment.getTask().getTaskCode());
        attachment.setTask(task);
        attachmentRepository.save(attachment);
        return attachment;
    }

    @Override
    public Attachment updateAttachmentByCode(String attachmentCode, Attachment attachment) {
        List<Attachment> attachments = attachmentRepository.findAll();
        Attachment attachmentExisting = validateAttachmentByCode(attachmentCode);

        List<Attachment> attachmentsWithoutAttachmentCodeChosed = new ArrayList<>();

        for(Attachment t : attachments){
            if (!Objects.equals(t.getAttachmentCode(), attachmentCode)){
                attachmentsWithoutAttachmentCodeChosed.add(t);
            }
        }
        attachmentExisting.setAttachmentCode(attachment.getAttachmentCode());
        attachmentExisting.setName(attachment.getName());
        attachmentExisting.setExtension(attachment.getExtension());

        Task task = taskServiceImpl.validateTaskByCode(attachment.getTask().getTaskCode());
        attachmentExisting.setTask(task);

        if(attachmentsWithoutAttachmentCodeChosed.isEmpty()){
            attachmentRepository.save(attachmentExisting);
        } else {
            for(Attachment t : attachmentsWithoutAttachmentCodeChosed){
                if(t.getAttachmentCode().equals(attachmentExisting.getAttachmentCode())){
                    throw new DuplicateException("CODE: " + attachmentCode, "CODE: " + attachment.getAttachmentCode());
                }
            }
            attachmentRepository.save(attachmentExisting);
        }

        return attachmentExisting;
    }

    @Override
    public void deleteAttachmentByCode(String attachmentCode) {
        Attachment attachment = validateAttachmentByCode(attachmentCode);
        attachmentRepository.deleteById(attachment.getId());
    }



    public Attachment validateAttachmentByCode(String code) {
        Attachment existingCode = attachmentRepository.findAttachmentByAttachmentCode(code);

        if (Objects.isNull(existingCode)) {
            throw new ResourceNotFoundException("Error: Attachment with CODE: " + code + " not found.");
        }
        return existingCode;
    }
    public void validateAttachmentFields(AttachmentDTO attachmentDTO) {
        if (Strings.isEmpty(attachmentDTO.getAttachmentCode()) ||
                Strings.isEmpty(attachmentDTO.getName()) ||
                Strings.isEmpty(attachmentDTO.getExtension()) ||
                Strings.isEmpty(attachmentDTO.getTaskCode())) {
            throw new IllegalArgumentException("Error: The fields of the attachment can't be null or empty.");
        }
    }
    private void checkForDuplicateAttachment(String attachmentCode) {
        Attachment attachmentByCode = attachmentRepository.findAttachmentByAttachmentCode(attachmentCode);
        if (!Objects.isNull(attachmentByCode)) {
            throw new DuplicateException("CODE: " + attachmentCode, "CODE: " + attachmentByCode.getAttachmentCode());
        }
    }
}
