package com.fincons.taskmanager.service.boardLaneService;

import com.fincons.taskmanager.entity.BoardLane;

public interface BoardLaneService {

    BoardLane createBoardLane(BoardLane boardLane);
    BoardLane updateBoardLane(String boardCode, String laneCode, BoardLane boardLane);
    BoardLane deleteBoardLane(String boardCode, String laneCode);


}
