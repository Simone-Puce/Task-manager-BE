package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AttachmentRepository extends JpaRepository<Attachment,Long> {
    Attachment findAttachmentByAttachmentIdAndActiveTrue(Long id);
    List<Attachment> findAllByActiveTrue();

}
