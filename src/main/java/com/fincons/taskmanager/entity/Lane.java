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
@Table(name = "lane")
public class Lane {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long laneId;

    @Column(nullable = false)
    private String laneName;

    @ManyToOne
    @JoinColumn(name = "board_id")
    private Board board;

    @OneToMany(
            mappedBy = "lane",
            fetch = FetchType.LAZY)
    private List<Task> tasks;

    private boolean active;

    public Lane(Long laneId, String laneName) {
        this.laneId = laneId;
        this.laneName = laneName;
    }
}
