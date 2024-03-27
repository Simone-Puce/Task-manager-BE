package com.fincons.taskmanager.service.attachmentService;


import com.fincons.taskmanager.entity.Attachment;
import com.fincons.taskmanager.entity.AttachmentDownload;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AttachmentService {

    Attachment getAttachmentById(Long attachmentId);
    AttachmentDownload downloadFile(Long id);
    Attachment uploadAttachment(Long taskId, MultipartFile file) throws IOException;
    void deleteAttachmentById(Long attachmentId);
}
