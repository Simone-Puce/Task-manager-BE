package com.fincons.taskmanager.service.attachmentService.impl;


import com.fincons.taskmanager.entity.Attachment;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.AttachmentRepository;
import com.fincons.taskmanager.service.attachmentService.AttachmentService;
import com.fincons.taskmanager.service.taskService.impl.TaskServiceImpl;
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
        return attachmentRepository.findAllByActiveTrue();
    }

    @Override
    public Attachment createAttachment(Attachment attachment) {
        checkForDuplicateAttachment(attachment.getAttachmentCode());

        Task task = taskServiceImpl.validateTaskByCode(attachment.getTask().getTaskCode());
        attachment.setTask(task);
        attachment.setActive(true);
        attachmentRepository.save(attachment);
        return attachment;
    }

    @Override
    public Attachment updateAttachmentByCode(String attachmentCode, Attachment attachment) {
        List<Attachment> attachments = attachmentRepository.findAll();
        Attachment attachmentExisting = validateAttachmentByCode(attachmentCode);

        List<Attachment> attachmentsExcludingSelectedAttachments = new ArrayList<>();

        for(Attachment t : attachments){
            if (!Objects.equals(t.getAttachmentCode(), attachmentCode)){
                attachmentsExcludingSelectedAttachments.add(t);
            }
        }
        attachmentExisting.setAttachmentCode(attachment.getAttachmentCode());
        attachmentExisting.setAttachmentName(attachment.getAttachmentName());
        attachmentExisting.setExtension(attachment.getExtension());

        Task task = taskServiceImpl.validateTaskByCode(attachment.getTask().getTaskCode());
        attachmentExisting.setTask(task);

        if(attachmentsExcludingSelectedAttachments.isEmpty()){
            attachmentRepository.save(attachmentExisting);
        } else {
            for(Attachment t : attachmentsExcludingSelectedAttachments){
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
        attachment.setActive(false);
        attachmentRepository.save(attachment);
    }

    public Attachment validateAttachmentByCode(String code) {
        Attachment existingCode = attachmentRepository.findAttachmentByAttachmentCodeAndActiveTrue(code);

        if (Objects.isNull(existingCode)) {
            throw new ResourceNotFoundException("Error: Attachment with CODE: " + code + " not found.");
        }
        return existingCode;
    }
    private void checkForDuplicateAttachment(String attachmentCode) {
        Attachment attachmentByCode = attachmentRepository.findAttachmentByAttachmentCodeAndActiveTrue(attachmentCode);
        if (!Objects.isNull(attachmentByCode)) {
            throw new DuplicateException("CODE: " + attachmentCode, "CODE: " + attachmentByCode.getAttachmentCode());
        }
    }
}
