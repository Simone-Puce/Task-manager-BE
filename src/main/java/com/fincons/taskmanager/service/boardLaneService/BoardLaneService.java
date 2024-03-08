package com.fincons.taskmanager.service.boardLaneService;

import com.fincons.taskmanager.entity.BoardLane;

public interface BoardLaneService {

    BoardLane createBoardLane(BoardLane boardLane);
    BoardLane updateBoardLane(Long boardId, Long laneId, BoardLane boardLane);
    BoardLane deleteBoardLane(Long boardId, Long laneId);


}
