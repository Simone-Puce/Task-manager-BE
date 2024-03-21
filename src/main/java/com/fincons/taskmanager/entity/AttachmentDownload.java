package com.fincons.taskmanager.entity;

import com.fincons.taskmanager.entity.Attachment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class AttachmentDownload {
    private byte[] byteArray;
    private Attachment attachment;
}
