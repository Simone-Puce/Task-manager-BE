package com.fincons.taskmanager.service.attachmentService.impl;

import com.fincons.taskmanager.entity.Attachment;

import java.util.Base64;

public class AttachmentDecoding {
    public static byte[] decodeToString(Attachment attachment){
        return Base64.getMimeDecoder().decode(attachment.getFile64());
    }
}