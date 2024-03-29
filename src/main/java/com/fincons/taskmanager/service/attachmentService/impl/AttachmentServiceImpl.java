package com.fincons.taskmanager.service.attachmentService.impl;


import com.fincons.taskmanager.entity.Attachment;
import com.fincons.taskmanager.entity.AttachmentDownload;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.enums.FileExtension;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.AttachmentRepository;
import com.fincons.taskmanager.service.attachmentService.AttachmentService;
import com.fincons.taskmanager.service.taskService.impl.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
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
    public AttachmentDownload downloadFile(Long attachmentId) {
        Attachment attachmentExisting = validateAttachmentById(attachmentId);
        return new AttachmentDownload(
                AttachmentDecoding.decodeToString(attachmentExisting), attachmentExisting);
    }

    @Override
    public List<Attachment> getAllAttachments() {
        return attachmentRepository.findAll();
    }

    @Override
    public Attachment uploadAttachment(Long taskId, MultipartFile file) throws IOException {
        Task task = taskServiceImpl.validateTaskById(taskId);
        String attachmentName = getFileName(file);
        String extension = getFileExtension(file);
        compatibleExtension(extension);
        String encodeAttachment = AttachmentEncoding.encodeAttachment(file);
        Attachment attachment = new Attachment(attachmentName, encodeAttachment, extension, task);
        attachmentRepository.save(attachment);
        return attachment;
    }
    @Override
    public void deleteAttachmentById(Long attachmentId) {
        Attachment attachment = validateAttachmentById(attachmentId);
        attachmentRepository.delete(attachment);
    }

    public Attachment validateAttachmentById(Long id) {
        Attachment existingId = attachmentRepository.findAttachmentByAttachmentId(id);

        if (Objects.isNull(existingId)) {
            throw new ResourceNotFoundException("Error: Attachment with ID: " + id + " not found.");
        }
        return existingId;
    }
    private String getFileName(MultipartFile file) throws IOException{
        String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.isEmpty()) {
            int lastIndex = fileName.lastIndexOf('.');
            if (lastIndex != -1) {
                return fileName.substring(0, lastIndex);
            } else {
                return fileName;
            }
        } else {
            throw new IOException("File can't be without a name");
        }
    }
    private String getFileExtension(MultipartFile file) throws IOException {
        String fileExtension = file.getOriginalFilename();
        if (fileExtension != null && fileExtension.contains(".")) {
            return fileExtension.substring(fileExtension.lastIndexOf(".") + 1);
        }
        else {
            throw new IOException("Invalid extension or file without extension");
        }
    }
    private void compatibleExtension(String inputExtension){
        List<String> extensions = FileExtension.extensions;
        if (!extensions.contains(inputExtension)) {
            StringBuilder message = new StringBuilder("Not compatible, the extensions compatible are: ");
            extensions.forEach(extension -> message.append(extension).append(", "));
            message.setLength(message.length() - 2);
            throw new IllegalArgumentException(message.toString());
        }
    }
}
