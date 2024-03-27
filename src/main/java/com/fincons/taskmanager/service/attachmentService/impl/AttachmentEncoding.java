package com.fincons.taskmanager.service.attachmentService.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;


public class AttachmentEncoding {
    private static final Logger log = LogManager.getLogger(AttachmentEncoding.class);

    public static String encodeAttachment(MultipartFile attachmentPath) throws IOException {
        log.debug("Before encoding: Try to do coding");
        byte[] sourceBytes = attachmentPath.getBytes();
        String encodedString = Base64.getEncoder().encodeToString(sourceBytes);
        log.info("After encoding: Attachment successfully encoded");
        return encodedString;
    }
}