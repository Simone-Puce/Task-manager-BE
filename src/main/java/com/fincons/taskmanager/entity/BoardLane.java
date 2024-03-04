package com.fincons.taskmanager.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "boards_lanes")
@Entity
public class BoardLane {

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    Long id;

    @ManyToOne
            @JoinColumn(name = "board_id")
            private Board board;

    @ManyToOne
            @JoinColumn(name = "lane_id")
            private Lane lane;

    public BoardLane(Board board, Lane lane) {
        this.board = board;
        this.lane = lane;
    }

}
