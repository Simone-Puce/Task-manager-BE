package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.BoardLane;
import com.fincons.taskmanager.entity.Lane;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLaneRepository extends JpaRepository<BoardLane, Long> {
    boolean existsByBoardAndLane(Board board, Lane lane);
    BoardLane findByBoardBoardCodeAndLaneLaneCode(String boardCode, String laneCode);

}
