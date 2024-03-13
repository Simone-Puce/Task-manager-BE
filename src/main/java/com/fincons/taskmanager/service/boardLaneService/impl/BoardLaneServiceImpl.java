package com.fincons.taskmanager.service.boardLaneService.impl;

import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.BoardLane;
import com.fincons.taskmanager.entity.Lane;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.BoardLaneRepository;
import com.fincons.taskmanager.repository.BoardRepository;
import com.fincons.taskmanager.repository.LaneRepository;
import com.fincons.taskmanager.service.boardLaneService.BoardLaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class BoardLaneServiceImpl implements BoardLaneService {

    @Autowired
    private LaneRepository laneRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private BoardLaneRepository boardLaneRepository;

    @Override
    public BoardLane createBoardLane(BoardLane boardLane) {
        Board existingBoard = validateBoardById(boardLane.getBoard().getBoardId());
        Lane existingLane = validateLaneById(boardLane.getLane().getLaneId());

        checkDuplicateBoardLaneExistAndActiveIsTrue(existingBoard, existingLane);
        BoardLane newBoardLane = new BoardLane(existingBoard, existingLane);
        boardLaneRepository.save(newBoardLane);
        return newBoardLane;
    }

    @Override
    public BoardLane updateBoardLane(Long boardId, Long laneId, BoardLane boardLane) {
        
        Board existingBoard = validateBoardById(boardId);
        Lane existingLane = validateLaneById(laneId);
        BoardLane boardLaneExist = validateBoardLaneRelationship(existingBoard, existingLane);

        Board boardToUpdate = validateBoardById(boardLane.getBoard().getBoardId());
        Lane laneToUpdate = validateLaneById(boardLane.getLane().getLaneId());

        boardLaneExist.setBoard(boardToUpdate);
        boardLaneExist.setLane(laneToUpdate);
        boardLaneRepository.save(boardLaneExist);
        return boardLaneExist;
    }
    @Override
    public BoardLane deleteBoardLane(Long boardId, Long laneId) {
        Board existingBoard = validateBoardById(boardId);
        Lane existingLane = validateLaneById(laneId);
        BoardLane boardLaneExist = validateBoardLaneRelationship(existingBoard, existingLane);
        boardLaneRepository.delete(boardLaneExist);
        return boardLaneExist;
    }
    private Board validateBoardById(Long id) {
        Board existingBoard = boardRepository.findBoardByBoardIdAndActiveTrue(id);

        if (Objects.isNull(existingBoard)) {
            throw new ResourceNotFoundException("Error: Board with ID: " + id + " not found.");
        }
        return existingBoard;
    }
    private Lane validateLaneById(Long id) {
        Lane existingLane = laneRepository.findLaneByLaneIdAndActiveTrue(id);

        if (Objects.isNull(existingLane)) {
            throw new ResourceNotFoundException("Error: Lane with ID: " + id + " not found.");
        }
        return existingLane;
    }
    private void checkDuplicateBoardLaneExistAndActiveIsTrue(Board board, Lane lane) {

        boolean boardLaneExist = boardLaneRepository.existsByBoardAndLane(board, lane);
        if (boardLaneExist) {
            throw new DuplicateException(
                    "board ID: " + board.getBoardId() + " and lane ID: " + lane.getLaneId(),
                    "board ID: " + board.getBoardId() + " and lane ID: " + lane.getLaneId());
        }
    }
    private BoardLane checkDuplicateBoardLaneExistAndActiveIsFalse(Board board, Lane lane) {
        return boardLaneRepository.findByBoardAndLane(board, lane);
    }
    private BoardLane validateBoardLaneRelationship(Board board, Lane lane) {
        BoardLane boardLaneExist = boardLaneRepository.findByBoardBoardIdAndLaneLaneId(board.getBoardId(), lane.getLaneId());
        if (Objects.isNull(boardLaneExist)) {
            throw new ResourceNotFoundException("Error: Relationship with ID board: " + board.getBoardId() +
                    " and ID lane: " + lane.getLaneId() + " don't exists.");
        }
        return boardLaneExist;
    }
    private void validateBoardLaneNotExistRelationship(Board board, Lane lane) {
        BoardLane boardLaneExist = boardLaneRepository.findByBoardBoardIdAndLaneLaneId(board.getBoardId(), lane.getLaneId());
        if (!Objects.isNull(boardLaneExist)) {
            throw new ResourceNotFoundException("Error: Relationship with ID board: " + board.getBoardId() +
                    " and ID lane: " + lane.getLaneId() + " already exist.");
        }
    }
}
