package com.fincons.taskmanager.service.boardService;


import com.fincons.taskmanager.entity.Board;

import java.util.List;

public interface BoardService {
    Board getBoardByCode(String boardCode);
    List<Board> getAllBoards();
    Board createBoard(Board board);
    Board updateBoardByCode(String boardCode, Board board);
    void deleteBoardByCode(String boardCode);
}
