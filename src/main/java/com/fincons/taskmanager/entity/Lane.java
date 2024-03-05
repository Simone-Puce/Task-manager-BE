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
    private Long id;

    @Column(unique = true, nullable = false)
    private String laneCode;

    @Column(nullable = false)
    private String laneName;
    @Column(name = "active")
    private boolean active;

    @OneToMany(
            mappedBy = "lane",
            fetch = FetchType.LAZY)
    private List<BoardLane> boardsLanes;
}
