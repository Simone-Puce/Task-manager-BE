package com.fincons.taskmanager.service.boardService.impl;


import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.BoardLane;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.BoardLaneRepository;
import com.fincons.taskmanager.repository.BoardRepository;
import com.fincons.taskmanager.repository.UserBoardRepository;
import com.fincons.taskmanager.service.boardService.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardLaneRepository boardLaneRepository;
    @Autowired
    private UserBoardRepository userBoardRepository;

    @Override
    public Board getBoardById(Long boardId) {
        existingBoardById(boardId);
        return getBoardByIdRelationshipsJoined(boardId);
    }

    @Override
    public List<Board> getAllBoards() {
        return getAllRelationshipsJoined();
    }

    @Override
    public Board createBoard(Board board) {
        board.setActive(true);
        boardRepository.save(board);
        return board;
    }

    @Override
    public Board updateBoardById(Long boardId, Board board) {
        existingBoardById(boardId);
        Board boardForLane = boardRepository.findBoardIdForLaneAllTrue(boardId);
        Board boardForTask = boardRepository.findBoardIdForTaskAllTrue(boardId);
        boardForLane.setBoardName(board.getBoardName());
        boardRepository.save(boardForLane);
        boardForLane.setTasks(boardForTask.getTasks());
        return boardForLane;
    }

    @Override
    @Transactional
    public void deleteBoardById(Long boardId) {
        Board board = validateBoardById(boardId);
        board.setActive(false);
        boardRepository.save(board);
        boardLaneRepository.deleteByBoard(board);
        userBoardRepository.deleteByBoard(board);
    }

    public Board validateBoardById(Long id) {
        Board existingId = boardRepository.findBoardByBoardIdAndActiveTrue(id);
        if (Objects.isNull(existingId)) {
            throw new ResourceNotFoundException("Error: Board with ID: " + id + " not found.");
        }
        return existingId;
    }
    private Board getBoardByIdRelationshipsJoined(Long boardId) {
        Board boardForLanes = boardRepository.findBoardIdForLaneAllTrue(boardId);
        Board boardForTasks = boardRepository.findBoardIdForTaskAllTrue(boardId);
        boardForLanes.setTasks(boardForTasks.getTasks());
        return boardForLanes;
    }
    private List<Board> getAllRelationshipsJoined() {
        List<Board> boardsForLanes = boardRepository.findAllForLaneAllTrue();
        List<Board> boardsForTasks = boardRepository.findAllForTaskAllTrue();
        boardsForLanes.forEach(board -> {
            List<Task> tasksForBoard = boardsForTasks.stream()
                    .filter(b -> b.getBoardId().equals(board.getBoardId()))
                    .flatMap(b -> b.getTasks().stream())
                    .collect(Collectors.toList());
            board.setTasks(tasksForBoard);
        });
        return boardsForLanes;
    }
    public void existingBoardById(Long id){
        boolean existingId = boardRepository.existsBoardByBoardIdAndActiveTrue(id);
        if (!existingId) {
            throw new ResourceNotFoundException("Error: Board with ID: " + id + " not found.");
        }
    }
}
