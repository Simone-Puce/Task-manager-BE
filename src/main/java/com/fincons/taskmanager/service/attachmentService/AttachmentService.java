package com.fincons.taskmanager.service.attachmentService;


import com.fincons.taskmanager.entity.Attachment;

import java.util.List;

public interface AttachmentService {

    Attachment getAttachmentById(Long attachmentId);
    List<Attachment> getAllAttachments();
    Attachment createAttachment(Attachment attachment);
    Attachment updateAttachmentById(Long attachmentId, Attachment attachment);
    void deleteAttachmentById(Long attachmentId);
}
