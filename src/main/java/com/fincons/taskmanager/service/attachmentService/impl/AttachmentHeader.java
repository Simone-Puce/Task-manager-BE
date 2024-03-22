package com.fincons.taskmanager.service.attachmentService.impl;

import com.fincons.taskmanager.entity.AttachmentDownload;
import com.fincons.taskmanager.enums.FileExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

public class AttachmentHeader {
    public static HttpHeaders createHeader(AttachmentDownload download){

        HttpHeaders headers = new HttpHeaders();

        if (FileExtension.PDF.equals(download.getAttachment().getExtension())) {
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentLength(download.getByteArray().length);
            headers.setContentDispositionFormData("attachment", download.getAttachment().getAttachmentName() + ".pdf");
        } else if (FileExtension.TEXT.equals(download.getAttachment().getExtension())) {
            headers.setContentType(MediaType.TEXT_PLAIN);
            headers.setContentLength(download.getByteArray().length);
            headers.setContentDispositionFormData("attachment", download.getAttachment().getAttachmentName() + ".txt");
        } else {
            throw new IllegalArgumentException("Need attachment in pdf or txt format");
        }
        return headers;
    }
}
