package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttachmentRepository extends JpaRepository<Attachment,Long> {

    Attachment findAttachmentByAttachmentCode(String code);
}
