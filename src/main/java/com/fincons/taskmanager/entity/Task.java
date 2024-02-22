package com.fincons.taskmanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "task")
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

    @ManyToMany(fetch = FetchType.LAZY,
            cascade = {CascadeType.PERSIST , CascadeType.MERGE})
    @JoinTable(name = "user_task",
            joinColumns = {
                    @JoinColumn(name = "user_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "task_id", referencedColumnName = "id")
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


}
