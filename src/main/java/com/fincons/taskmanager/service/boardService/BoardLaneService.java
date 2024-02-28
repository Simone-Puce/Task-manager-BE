package com.fincons.taskmanager.service.boardService;

import com.fincons.taskmanager.entity.BoardLane;

import java.util.List;

public interface BoardLaneService {

    BoardLane createBoardLane(String boardCode, String laneCode);
    BoardLane updateBoardLane(String boardCode, String laneCode, BoardLane boardLane);
    BoardLane deleteBoardLane(String boardCode, String laneCode);


}
