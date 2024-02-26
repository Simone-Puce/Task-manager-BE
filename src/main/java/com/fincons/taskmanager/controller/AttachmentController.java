package com.fincons.taskmanager.controller;

import com.fincons.taskmanager.dto.AttachmentDTO;
import com.fincons.taskmanager.entity.Attachment;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.mapper.AttachmentMapper;
import com.fincons.taskmanager.service.attachmentService.AttachmentService;
import com.fincons.taskmanager.utility.GenericResponse;
import com.fincons.taskmanager.utility.ValidateFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/task-manager")
public class AttachmentController {

    @Autowired
    private AttachmentService attachmentService;
    @Autowired
    private AttachmentMapper modelMapperAttachment;

    @GetMapping(value = "${attachment.find-by-code}")
    public ResponseEntity<GenericResponse<AttachmentDTO>> getAttachmentByCode(@RequestParam String code) {
        try {
            ValidateFields.validateSingleField(code);
            Attachment attachment = attachmentService.getAttachmentByCode(code);
            AttachmentDTO attachmentDTO = modelMapperAttachment.mapToDTO(attachment);
            GenericResponse<AttachmentDTO> response = GenericResponse.success(
                    attachmentDTO,
                    "Success: Found Attachment with CODE " + code + ".",
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.error(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        } catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rnfe.getMessage(),
                            HttpStatus.NOT_FOUND
                    )
            );
        }
    }

    @GetMapping(value = "${attachment.list}")
    public ResponseEntity<GenericResponse<List<AttachmentDTO>>> getAllAttachments() {
        List<Attachment> attachments = attachmentService.getAllAttachments();
        List<AttachmentDTO> attachmentDTOs = new ArrayList<>();
        for (Attachment attachment : attachments) {
            AttachmentDTO attachmentDTO = modelMapperAttachment.mapToDTO(attachment);
            attachmentDTOs.add(attachmentDTO);
        }
        GenericResponse<List<AttachmentDTO>> response = GenericResponse.success(
                attachmentDTOs,
                "Success:" + (attachmentDTOs.isEmpty() || attachmentDTOs.size() == 1 ? " Found " : " Founds ") + attachmentDTOs.size() +
                        (attachmentDTOs.isEmpty() || attachmentDTOs.size() == 1 ? " attachment" : " attachments") + ".",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = "${attachment.create}")
    public ResponseEntity<GenericResponse<AttachmentDTO>> createAttachment(@RequestBody AttachmentDTO attachmentDTO) {
        try {
            attachmentService.validateAttachmentFields(attachmentDTO);

            Attachment attachmentMapped = modelMapperAttachment.mapToEntity(attachmentDTO);

            Attachment attachment = attachmentService.createAttachment(attachmentMapped);

            AttachmentDTO attachmentDTO2 = modelMapperAttachment.mapToDTO(attachment);

            GenericResponse<AttachmentDTO> response = GenericResponse.success(
                    attachmentDTO2,
                    "Success: Attachment with code: " + attachment.getAttachmentCode() + " has been successfully updated!",
                    HttpStatus.OK);
            return ResponseEntity.ok(response);

        } catch (ResourceNotFoundException rfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rfe.getMessage(),
                            HttpStatus.NOT_FOUND
                    )
            );
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.error(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        } catch (DuplicateException dne) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    GenericResponse.error(
                            dne.getMessage(),
                            HttpStatus.CONFLICT
                    )
            );
        }
    }

    @PutMapping(value = "${attachment.put}")
    public ResponseEntity<GenericResponse<AttachmentDTO>> updateAttachmentByCode(@RequestParam String attachmentCode, @RequestBody AttachmentDTO attachmentDTO) {
        try {
            ValidateFields.validateSingleField(attachmentCode);

            attachmentService.validateAttachmentFields(attachmentDTO);

            Attachment attachmentMappedForService = modelMapperAttachment.mapToEntity(attachmentDTO);

            Attachment attachment = attachmentService.updateAttachmentByCode(attachmentCode, attachmentMappedForService);

            AttachmentDTO attachmentDTO2 = modelMapperAttachment.mapToDTO(attachment);

            GenericResponse<AttachmentDTO> response = GenericResponse.success(
                    attachmentDTO2,
                    "Success: Attachment with code: " + attachmentCode + " has been successfully updated!",
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException rfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rfe.getMessage(),
                            HttpStatus.NOT_FOUND
                    )
            );
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.error(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        } catch (DuplicateException dne) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    GenericResponse.error(
                            dne.getMessage(),
                            HttpStatus.CONFLICT
                    )
            );
        }
    }

    @DeleteMapping(value = "${attachment.delete}")
    public ResponseEntity<GenericResponse<AttachmentDTO>> deleteAttachmentByCode(@RequestParam String attachmentCode) {
        try {
            ValidateFields.validateSingleField(attachmentCode);
            attachmentService.deleteAttachmentByCode(attachmentCode);
            GenericResponse<AttachmentDTO> response = GenericResponse.empty(
                    "Success: Attachment with code: " + attachmentCode + " has been successfully deleted! ",
                    HttpStatus.OK
            );

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.error(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        } catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rnfe.getMessage(),
                            HttpStatus.NOT_FOUND));
        }
    }
}
