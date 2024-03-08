package com.fincons.taskmanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;


import java.sql.Timestamp;

import java.util.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "task")
@EntityListeners(AuditingEntityListener.class)
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long taskId;

    @Column(nullable = false)
    private String taskName;

    @Column(nullable = false)
    private String status;

    @Column
    private String description;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Timestamp modifiedDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;

    @Column(name = "active")
    private boolean active;

    @OneToMany(
            mappedBy = "task",
            fetch = FetchType.LAZY)
    private List<TaskUser> tasksUsers;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(
            mappedBy = "task",
            fetch = FetchType.LAZY)
    private List<Attachment> attachments;


    public Task(Long taskId, String taskName, String status, String description, Board board) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.status = status;
        this.description = description;
        this.board = board;
    }


    public Task(Long taskId, String taskName, String status, String description) {
        this.taskId = taskId;
        this.taskName = taskName;
        this.status = status;
        this.description = description;
    }

    public Task(String taskName, String status, String description) {
        this.taskName = taskName;
        this.status = status;
        this.description = description;
    }
}
