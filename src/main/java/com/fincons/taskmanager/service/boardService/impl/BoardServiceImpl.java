package com.fincons.taskmanager.service.boardService.impl;


import com.fincons.taskmanager.dto.BoardDTO;
import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.BoardRepository;
import com.fincons.taskmanager.service.boardService.BoardService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        return null;
    }

    @Override
    public void deleteBoardByCode(String boardCode) {

    }

    public Board validateBoardByCode(String code) {
        Board existingCode = boardRepository.findBoardByBoardCode(code);

        if (Objects.isNull(existingCode)) {
            throw new ResourceNotFoundException("Error: Board with CODE: " + code + " not found.");
        }
        return existingCode;
    }
    public void validateBoardFields(BoardDTO boardDTO) {
        if (Strings.isEmpty(boardDTO.getBoardCode()) ||
                Strings.isEmpty(boardDTO.getName())) {
            throw new IllegalArgumentException("Error: The fields of the board can't be null or empty.");
        }
    }
    private void checkForDuplicateBoard(String boardCode) {
        Board boardByCode = boardRepository.findBoardByBoardCode(boardCode);
        if (!Objects.isNull(boardByCode)) {
            throw new DuplicateException("CODE: " + boardCode, "CODE: " + boardByCode.getBoardCode());
        }
    }

}
