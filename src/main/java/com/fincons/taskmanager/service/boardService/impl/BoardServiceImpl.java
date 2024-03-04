package com.fincons.taskmanager.service.boardService.impl;


import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.BoardRepository;
import com.fincons.taskmanager.service.boardService.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepository;

    @Override
    public Board getBoardByCode(String boardCode) {
        return validateBoardByCode(boardCode);
    }

    @Override
    public List<Board> getAllBoards() {
        return boardRepository.findAll();
    }

    @Override
    public Board createBoard(Board board) {
        checkForDuplicateBoard(board.getBoardCode());
        boardRepository.save(board);
        return board;
    }

    @Override
    public Board updateBoardByCode(String boardCode, Board board) {
        List<Board> tasks = boardRepository.findAll();
        Board taskExisting = validateBoardByCode(boardCode);

        List<Board> boardExcludingSelectedBoard = new ArrayList<>();

        for(Board t : tasks){
            if (!Objects.equals(t.getBoardCode(), boardCode)){
                boardExcludingSelectedBoard.add(t);
            }
        }
        taskExisting.setBoardCode(board.getBoardCode());
        taskExisting.setBoardName(board.getBoardName());


        if(boardExcludingSelectedBoard.isEmpty()){
            boardRepository.save(taskExisting);
        } else {
            for(Board t : boardExcludingSelectedBoard){
                if(t.getBoardCode().equals(taskExisting.getBoardCode())){
                    throw new DuplicateException("CODE: " + boardCode, "CODE: " + board.getBoardCode());
                }
            }
            boardRepository.save(taskExisting);
        }
        return taskExisting;
    }

    @Override
    public void deleteBoardByCode(String boardCode) {
        Board board = validateBoardByCode(boardCode);
        boardRepository.deleteById(board.getId());
    }

    public Board validateBoardByCode(String code) {
        Board existingCode = boardRepository.findBoardByBoardCode(code);

        if (Objects.isNull(existingCode)) {
            throw new ResourceNotFoundException("Error: Board with CODE: " + code + " not found.");
        }
        return existingCode;
    }
    private void checkForDuplicateBoard(String boardCode) {
        Board boardByCode = boardRepository.findBoardByBoardCode(boardCode);
        if (!Objects.isNull(boardByCode)) {
            throw new DuplicateException("CODE: " + boardCode, "CODE: " + boardByCode.getBoardCode());
        }
    }
}
