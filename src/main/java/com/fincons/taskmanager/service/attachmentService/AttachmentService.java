package com.fincons.taskmanager.service.attachmentService;


import com.fincons.taskmanager.dto.AttachmentDTO;
import com.fincons.taskmanager.entity.Attachment;

import java.util.List;

public interface AttachmentService {

    Attachment getAttachmentByCode(String attachmentCode);
    List<Attachment> getAllAttachments();
    Attachment createAttachment(Attachment attachment);
    Attachment updateAttachmentByCode(String attachmentCode, Attachment attachment);
    void deleteAttachmentByCode(String attachmentCode);
    void validateAttachmentFields(AttachmentDTO attachmentDTO);
}
