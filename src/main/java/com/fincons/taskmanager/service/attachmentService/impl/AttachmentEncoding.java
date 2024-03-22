package com.fincons.taskmanager.service.attachmentService.impl;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;


public class AttachmentEncoding {
    public static String encodeAttachment(MultipartFile attachmentPath) throws IOException {
        byte[] sourceBytes = attachmentPath.getBytes();
        return Base64.getEncoder().encodeToString(sourceBytes);
    }
}