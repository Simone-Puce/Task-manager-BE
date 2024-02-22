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
@Table(name = "board")
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
    @JoinTable(name = "board_lane",
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
}
