package com.fincons.taskmanager.service.attachmentService.impl;


import com.fincons.taskmanager.entity.*;
import com.fincons.taskmanager.enums.FileExtension;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.exception.RoleException;
import com.fincons.taskmanager.repository.AttachmentRepository;
import com.fincons.taskmanager.service.attachmentService.AttachmentService;
import com.fincons.taskmanager.service.taskService.impl.TaskServiceImpl;
import com.fincons.taskmanager.service.userBoardService.UserBoardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Service
public class AttachmentServiceImpl implements AttachmentService {


    private static final String EDITOR = "EDITOR";
    @Autowired
    private UserBoardService userBoardService;
    @Autowired
    private AttachmentRepository attachmentRepository;
    @Autowired
    private TaskServiceImpl taskServiceImpl;
    private static final Logger log = LogManager.getLogger(AttachmentService.class);

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
    public Attachment uploadAttachment(Long taskId, MultipartFile file) throws IOException, RoleException {
        Task task = taskServiceImpl.validateTaskById(taskId);

        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean userAssociated = task.getTasksUsers().stream()
                .anyMatch(taskUser -> Objects.equals(taskUser.getUser().getEmail(), loggedUser));
        Lane laneToGetBoard = task.getLane();
        long boardId = laneToGetBoard.getBoard().getBoardId();
        List<UserBoard> userBoards = userBoardService.findBoardsByUser(loggedUser);
        List<UserBoard> userInBoard = userBoards.stream().filter(userBoardToCheck -> userBoardToCheck.getBoard().getBoardId() == boardId).toList();

        UserBoard userBoard = userInBoard.get(0);
        boolean userIsEditor = false;
        String roleCode = userBoard.getRoleCode();
        log.info(userBoard.getRoleCode());
        if(EDITOR.equals(roleCode)){
            userIsEditor = true;
        }
        if (!userAssociated && !userIsEditor){
            throw new RoleException("You are not associated with this task and cannot delete it.");
        }
        String attachmentName = getFileName(file);
        String extension = getFileExtension(file);
        compatibleExtension(extension);
        String encodeAttachment = AttachmentEncoding.encodeAttachment(file);
        Attachment attachment = new Attachment(attachmentName, encodeAttachment, extension, task);
        attachmentRepository.save(attachment);
        log.info("New attachment saved in the repository with ID {}.", attachment.getAttachmentId());
        return attachment;
    }
    @Override
    public void deleteAttachmentById(Long attachmentId) throws RoleException {
        Attachment attachment = validateAttachmentById(attachmentId);
        Task task = taskServiceImpl.validateTaskById(attachment.getTask().getTaskId());
        String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean userAssociated = task.getTasksUsers().stream()
                .anyMatch(taskUser -> Objects.equals(taskUser.getUser().getEmail(), loggedUser));
        Lane laneToGetBoard = task.getLane();
        long boardId = laneToGetBoard.getBoard().getBoardId();
        List<UserBoard> userBoards = userBoardService.findBoardsByUser(loggedUser);
        List<UserBoard> userInBoard = userBoards.stream().filter(userBoardToCheck -> userBoardToCheck.getBoard().getBoardId() == boardId).toList();

        UserBoard userBoard = userInBoard.get(0);
        boolean userIsEditor = false;
        String roleCode = userBoard.getRoleCode();
        log.info(userBoard.getRoleCode());
        if(EDITOR.equals(roleCode)){
            userIsEditor = true;
        }
        if (!userAssociated && !userIsEditor){
            throw new RoleException("You are not associated with this task and cannot delete attachments on it.");
        }
        attachmentRepository.delete(attachment);
        log.info("Attachment with ID {} deleted from the repository.", attachment.getAttachmentId());
    }

    public Attachment validateAttachmentById(Long id) {
        Attachment existingAttachment  = attachmentRepository.findAttachmentByAttachmentId(id);
        if (Objects.isNull(existingAttachment)) {
            throw new ResourceNotFoundException("Error: Attachment with ID: " + id + " not found.");
        }
        log.info("Attachment retrieved from repository with ID: {}", existingAttachment.getAttachmentId());
        return existingAttachment;
    }
    private String getFileName(MultipartFile file) throws IOException{
        String fileName = file.getOriginalFilename();
        if (fileName != null && !fileName.isEmpty()) {
            int lastIndex = fileName.lastIndexOf('.');
            if (lastIndex != -1) {
                String nameWithoutExtension = fileName.substring(0, lastIndex);
                log.debug("Get name without extension: {}", nameWithoutExtension);
                return nameWithoutExtension;
            } else {
                log.debug("Get name without extension: {}", fileName);
                return fileName;
            }
        } else {
            throw new IOException("File can't be without a name");
        }
    }
    private String getFileExtension(MultipartFile file) throws IOException {
        String fileExtension = file.getOriginalFilename();
        if (fileExtension != null && fileExtension.contains(".")) {
            String extensionFile = fileExtension.substring(fileExtension.lastIndexOf(".") + 1);
            log.debug("Get extension: {}", extensionFile);
            return extensionFile;
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
