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
    public Attachment getAttachmentById(Long attachmentId) {
        return validateAttachmentById(attachmentId);
    }

    @Override
    public List<Attachment> getAllAttachments() {
        return attachmentRepository.findAllByActiveTrue();
    }

    @Override
    public Attachment createAttachment(Attachment attachment) {
        Task task = taskServiceImpl.validateTaskById(attachment.getTask().getTaskId());
        attachment.setTask(task);
        attachment.setActive(true);
        attachmentRepository.save(attachment);
        return attachment;
    }

    @Override
    public Attachment updateAttachmentById(Long attachmentId, Attachment attachment) {
        Attachment attachmentExisting = validateAttachmentById(attachmentId);
        attachmentExisting.setAttachmentName(attachment.getAttachmentName());
        attachmentExisting.setExtension(attachment.getExtension());
        Task task = taskServiceImpl.validateTaskById(attachment.getTask().getTaskId());
        attachmentExisting.setTask(task);
        attachmentRepository.save(attachmentExisting);
        return attachmentExisting;
    }

    @Override
    public void deleteAttachmentById(Long attachmentId) {
        Attachment attachment = validateAttachmentById(attachmentId);
        attachment.setActive(false);
        attachmentRepository.save(attachment);
    }

    public Attachment validateAttachmentById(Long id) {
        Attachment existingId = attachmentRepository.findAttachmentByAttachmentIdAndActiveTrue(id);

        if (Objects.isNull(existingId)) {
            throw new ResourceNotFoundException("Error: Attachment with ID: " + id + " not found.");
        }
        return existingId;
    }
}
