package com.fincons.taskmanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "attachment")
public class Attachment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long attachmentId;

    @Column(nullable = false)
    private String attachmentName;

    @Column(nullable = false)
    private String extension;
    @Column(name = "active")
    private boolean active;

    @ManyToOne
    @JoinColumn(name = "task_id")
    private Task task;
}
