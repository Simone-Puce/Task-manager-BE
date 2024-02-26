package com.fincons.taskmanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.List;
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
    private long id;

    @Column(unique = true, nullable = false)
    private String boardCode;

    @Column(nullable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY,
        cascade = {CascadeType.PERSIST , CascadeType.MERGE})
    @JoinTable(name = "boards_lanes",
            joinColumns = {
                    @JoinColumn(name = "board_id", referencedColumnName = "id")
            },
            inverseJoinColumns = {
                    @JoinColumn(name = "lane_id", referencedColumnName = "id")
            }
    )
    private List<Lane> lanes;

    @OneToMany(
            mappedBy = "board",
            fetch = FetchType.LAZY)
    private List<Task> tasks;

    @ManyToMany(mappedBy = "boards")
    private List<User> users;

    @CreatedDate
    private long createdDate;

    @LastModifiedDate
    private long modifiedDate;

    public Board(long id, String boardCode, String name) {
        this.id = id;
        this.boardCode = boardCode;
        this.name = name;
    }
}
