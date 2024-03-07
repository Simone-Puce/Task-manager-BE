package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment,Long> {

    Attachment findAttachmentByAttachmentCode(String code);
    Attachment findAttachmentByAttachmentCodeAndActiveTrue(String code);
    List<Attachment> findAllByActiveTrue();

}
