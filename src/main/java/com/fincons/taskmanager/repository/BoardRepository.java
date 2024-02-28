package com.fincons.taskmanager.repository;


import com.fincons.taskmanager.dto.BoardLaneDTO;
import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.BoardLane;
import com.fincons.taskmanager.entity.Lane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Board findBoardByBoardCode(String code);
/*
    @Query(
            "SELECT NEW com.fincons.taskmanager.dto.BoardLaneDTO(b.boardCode, b.name, l.laneCode, l.name)" +
                    "FROM Board b " +
                    "JOIN b.lanes l"
    )
    List<BoardLaneDTO> getAllBoardLane();
*/

}
