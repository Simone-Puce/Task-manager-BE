package com.fincons.taskmanager.service.boardService;


import com.fincons.taskmanager.entity.Board;

import java.util.List;

public interface BoardService {
    Board getBoardById(Long boardId);
    List<Board> getAllBoards();
    Board createBoard(Board board);
    Board updateBoardById(Long boardId, Board board);
    void deleteBoardById(Long boardId);
}
