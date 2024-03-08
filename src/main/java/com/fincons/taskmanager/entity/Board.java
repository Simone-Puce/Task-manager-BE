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
import java.util.List;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "board")
@EntityListeners(AuditingEntityListener.class)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long boardId;

    @Column(name = "name", nullable = false)
    private String boardName;

    @OneToMany(
            mappedBy = "board",
            fetch = FetchType.LAZY)
    private List<BoardLane> boardsLanes;

    @OneToMany(
            mappedBy = "board",
            fetch = FetchType.LAZY)
    private List<Task> tasks;

    @OneToMany(mappedBy = "board",
            fetch = FetchType.LAZY)
    private List<UserBoard> usersBoards;

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

    public Board(Long boardId, String boardName, List<Task> tasks) {
        this.boardId = boardId;
        this.boardName = boardName;
        this.tasks = tasks;
    }

}
