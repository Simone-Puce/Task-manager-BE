package com.fincons.taskmanager.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "board_lane")
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