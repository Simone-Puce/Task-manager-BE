package com.fincons.taskmanager.controller;

import com.fincons.taskmanager.dto.AttachmentDTO;
import com.fincons.taskmanager.dto.AuditDTO;
import com.fincons.taskmanager.entity.Attachment;
import com.fincons.taskmanager.service.AuditServiceImpl;
import com.fincons.taskmanager.utility.GenericResponse;
import com.fincons.taskmanager.utility.ValidateFields;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/task-manager")
public class AuditController {

    @Autowired
    private AuditServiceImpl auditServiceImpl;

    @GetMapping(value = "${audit.get-all}")
    public ResponseEntity<GenericResponse<AuditDTO>> getAttachmentById() {
        AuditDTO auditDTO = auditServiceImpl.getAudit();
        GenericResponse<AuditDTO> response = GenericResponse.success(
                auditDTO,
                "Success: Audit generated",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }

}
