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
    public BoardLane createBoardLane(String boardCode, String laneCode) {

        Board existingBoard = validateBoardByCode(boardCode);
        Lane existingLane = validateLaneByCode(laneCode);
        checkDuplicateBoardLaneExist(existingBoard, existingLane);
        BoardLane boardLane = new BoardLane(existingBoard, existingLane);
        boardLaneRepository.save(boardLane);
        return boardLane;
    }

    @Override
    public BoardLane updateBoardLane(String boardCode, String laneCode, BoardLane boardLane) {

        Board existingBoard = validateBoardByCode(boardCode);
        Lane existingLane = validateLaneByCode(laneCode);
        BoardLane boardLaneExist = validateBoardLaneRelationship(existingBoard, existingLane);
        Board boardToUpdate = validateBoardByCode(boardLane.getBoard().getBoardCode());
        Lane laneToUpdate = validateLaneByCode(boardLane.getLane().getLaneCode());
        validateBoardLaneNotExistRelationship(boardToUpdate, laneToUpdate);
        boardLaneExist.setBoard(boardToUpdate);
        boardLaneExist.setLane(laneToUpdate);
        boardLaneRepository.save(boardLaneExist);
        return boardLaneExist;
    }
    @Override
    public BoardLane deleteBoardLane(String boardCode, String laneCode) {

        Board existingBoard = validateBoardByCode(boardCode);
        Lane existingLane = validateLaneByCode(laneCode);
        BoardLane boardLaneExist = validateBoardLaneRelationship(existingBoard, existingLane);
        boardLaneRepository.delete(boardLaneExist);
        return boardLaneExist;
    }
    private Board validateBoardByCode(String code) {
        Board existingBoard = boardRepository.findBoardByBoardCode(code);

        if (Objects.isNull(existingBoard)) {
            throw new ResourceNotFoundException("Error: Board with CODE: " + code + " not found.");
        }
        return existingBoard;
    }
    private Lane validateLaneByCode(String code) {
        Lane existingLane = laneRepository.findLaneByLaneCode(code);

        if (Objects.isNull(existingLane)) {
            throw new ResourceNotFoundException("Error: Lane with CODE: " + code + " not found.");
        }
        return existingLane;
    }
    private void checkDuplicateBoardLaneExist(Board board, Lane lane) {

        boolean boardLaneExist = boardLaneRepository.existsByBoardAndLane(board, lane);
        if (boardLaneExist) {
            throw new DuplicateException(
                    "board CODE: " + board.getBoardCode() + " and lane CODE: " + lane.getLaneCode(),
                    "board CODE: " + board.getBoardCode() + " and lane CODE: " + lane.getLaneCode());
        }
    }
    private BoardLane validateBoardLaneRelationship(Board board, Lane lane) {
        BoardLane boardLaneExist = boardLaneRepository.findByBoardBoardCodeAndLaneLaneCode(board.getBoardCode(), lane.getLaneCode());
        if (Objects.isNull(boardLaneExist)) {
            throw new ResourceNotFoundException("Error: Relationship with CODE board: " + board.getBoardCode() +
                    " and CODE lane: " + lane.getLaneCode() + " don't exists.");
        }
        return boardLaneExist;
    }
    private void validateBoardLaneNotExistRelationship(Board board, Lane lane) {
        BoardLane boardLaneExist = boardLaneRepository.findByBoardBoardCodeAndLaneLaneCode(board.getBoardCode(), lane.getLaneCode());
        if (!Objects.isNull(boardLaneExist)) {
            throw new ResourceNotFoundException("Error: Relationship with CODE board: " + board.getBoardCode() +
                    " and CODE lane: " + lane.getLaneCode() + " already exist.");
        }
    }
}
