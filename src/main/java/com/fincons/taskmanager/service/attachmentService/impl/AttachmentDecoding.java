package com.fincons.taskmanager.service.attachmentService.impl;

import com.fincons.taskmanager.entity.Attachment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Base64;

public class AttachmentDecoding {
    private static final Logger log = LoggerFactory.getLogger(AttachmentDecoding.class);
    public static byte[] decodeToString(Attachment attachment){
        log.debug("Before decoding: Try to decode attachment");
        byte[] decodedBytes = Base64.getMimeDecoder().decode(attachment.getFile64());
        log.info("After decoding: Attachment successfully decoded");
        return decodedBytes;
    }
}