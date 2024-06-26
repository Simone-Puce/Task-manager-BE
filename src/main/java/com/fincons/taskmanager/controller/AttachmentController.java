package com.fincons.taskmanager.controller;

import com.fincons.taskmanager.dto.AttachmentDTO;
import com.fincons.taskmanager.entity.Attachment;
import com.fincons.taskmanager.exception.RoleException;
import com.fincons.taskmanager.mapper.AttachmentMapper;
import com.fincons.taskmanager.service.attachmentService.AttachmentService;
import com.fincons.taskmanager.entity.AttachmentDownload;
import com.fincons.taskmanager.service.attachmentService.impl.AttachmentHeader;
import com.fincons.taskmanager.utility.GenericResponse;
import com.fincons.taskmanager.utility.ValidateFields;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@RestController
@CrossOrigin("*")
@RequestMapping("/task-manager")
public class AttachmentController {
    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private AttachmentMapper modelMapperAttachment;
    private static final Logger log = LogManager.getLogger(AttachmentController.class);
    @GetMapping(value = "${attachment.find-by-id}")
    public ResponseEntity<GenericResponse<AttachmentDTO>> getAttachmentById(@RequestParam Long id) {
        log.info("Received {} request for attachment with ID: {}", RequestMethod.GET, id);
        ValidateFields.validateSingleFieldLong(id);
        Attachment attachment = attachmentService.getAttachmentById(id);
        AttachmentDTO attachmentDTO = modelMapperAttachment.mapToDTO(attachment);
        GenericResponse<AttachmentDTO> response = GenericResponse.success(
                attachmentDTO,
                "Success: Found Attachment with ID " + id + ".",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }
    @GetMapping(value = "${attachment.download-by-id}")
    public ResponseEntity<byte[]> downloadAttachmentById(@RequestParam Long id) {
        log.info("Received {} request for attachment with ID: {}", RequestMethod.GET, id);
        ValidateFields.validateSingleFieldLong(id);
        AttachmentDownload download = attachmentService.downloadFile(id);
        HttpHeaders headers = AttachmentHeader.createHeader(download);
        return ResponseEntity.ok().headers(headers).body(download.getByteArray());
    }
    @PostMapping(value = "${attachment.upload}")
    public ResponseEntity<GenericResponse<AttachmentDTO>> uploadAttachment(@RequestParam Long taskId,
                                                                           @RequestBody MultipartFile file) throws IOException, RoleException {
        log.info("Received {} request to upload with a reference to Task with ID: {}", RequestMethod.POST, taskId);
        ValidateFields.validateSingleFieldLong(taskId);
        if(Objects.isNull(file)){
            throw new IOException("The file cannot be null");
        }
        Attachment attachment = attachmentService.uploadAttachment(taskId, file);
        AttachmentDTO attachmentDTO2 = modelMapperAttachment.mapToDTO(attachment);
        GenericResponse<AttachmentDTO> response = GenericResponse.success(
                attachmentDTO2,
                "Success: Attachment with id: " + attachment.getAttachmentId() + " has been successfully updated!",
                HttpStatus.OK);
        return ResponseEntity.ok(response);
    }
    @DeleteMapping(value = "${attachment.delete}")
    public ResponseEntity<GenericResponse<AttachmentDTO>> deleteAttachmentById(@RequestParam Long attachmentId) throws RoleException {
        log.info("Received {} request for delete Attachment with ID: {}", RequestMethod.DELETE, attachmentId);
        ValidateFields.validateSingleFieldLong(attachmentId);
        attachmentService.deleteAttachmentById(attachmentId);
        GenericResponse<AttachmentDTO> response = GenericResponse.empty(
                "Success: Attachment with id: " + attachmentId + " has been successfully deleted! ",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }
}
