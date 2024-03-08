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
    public Board getBoardById(Long boardId) {
        return validateBoardById(boardId);
    }

    @Override
    public List<Board> getAllBoards() {
        return boardRepository.findAllByActiveTrue();
    }

    @Override
    public Board createBoard(Board board) {
        board.setActive(true);
        boardRepository.save(board);
        return board;
    }

    @Override
    public Board updateBoardById(Long boardId, Board board) {
        Board taskExisting = validateBoardById(boardId);
        taskExisting.setBoardName(board.getBoardName());
        boardRepository.save(taskExisting);
        return taskExisting;
    }

    @Override
    public void deleteBoardById(Long boardId) {
        Board board = validateBoardById(boardId);
        board.setActive(false);
        boardRepository.save(board);
    }

    public Board validateBoardById(Long id) {
        Board existingId = boardRepository.findBoardByBoardIdAndActiveTrue(id);
        if (Objects.isNull(existingId)) {
            throw new ResourceNotFoundException("Error: Board with ID: " + id + " not found.");
        }
        return existingId;
    }
}
