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
    private long id;

    @Column(unique = true, nullable = false)
    private String taskCode;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String status;

    @Column
    private String description;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "modified_date")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date modifiedDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String modifiedBy;



    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST , CascadeType.MERGE})
    @JoinTable(name = "tasks_users",
            joinColumns = {
                    @JoinColumn(name = "task_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id")
            }
    )
    private List<User> users;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(
            mappedBy = "task",
            fetch = FetchType.LAZY)
    private List<Attachment> attachments;

    public Task(long id, String taskCode, String name, String status, String description, Board board) {
        this.id = id;
        this.taskCode = taskCode;
        this.name = name;
        this.status = status;
        this.description = description;
        this.board = board;
    }

    public Task(long id, String taskCode, String name, String status, String description, List<User> users, Board board) {
        this.id = id;
        this.taskCode = taskCode;
        this.name = name;
        this.status = status;
        this.description = description;
        this.users = users;
        this.board = board;
    }
    public Task(long id, String taskCode, String name, String status, String description) {
        this.id = id;
        this.taskCode = taskCode;
        this.name = name;
        this.status = status;
        this.description = description;
    }

    public Task(String taskCode, String name, String status, String description) {
        this.taskCode = taskCode;
        this.name = name;
        this.status = status;
        this.description = description;
    }
}
