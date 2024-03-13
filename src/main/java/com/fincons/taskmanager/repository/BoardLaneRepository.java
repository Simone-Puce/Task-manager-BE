package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.BoardLane;
import com.fincons.taskmanager.entity.Lane;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLaneRepository extends JpaRepository<BoardLane, Long> {

    void deleteByBoard(Board board);
    void deleteByLane(Lane lane);
    BoardLane findByBoardAndLane(Board board, Lane lane);
    boolean existsByBoardAndLane(Board board, Lane lane);
    BoardLane findByBoardBoardIdAndLaneLaneId(Long boardId, Long laneId);

}
