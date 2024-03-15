package com.fincons.taskmanager.service.boardService.impl;


import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.Lane;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.BoardRepository;
import com.fincons.taskmanager.repository.LaneRepository;
import com.fincons.taskmanager.repository.TaskRepository;
import com.fincons.taskmanager.repository.UserBoardRepository;
import com.fincons.taskmanager.service.boardService.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class BoardServiceImpl implements BoardService {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserBoardRepository userBoardRepository;
    @Autowired
    private LaneRepository laneRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public Board getBoardById(Long boardId) {
        existingBoardById(boardId);
        Board boardForLanes = boardRepository.findBoardIdForLaneAllTrue(boardId);
        if(Objects.isNull(boardForLanes)){
            Board boardForLaneNonNull = boardRepository.findBoardByBoardIdAndActiveTrue(boardId);
            return filterBoardForLanesTrue(boardForLaneNonNull);
        }
        return boardForLanes;
    }
    @Override
    public List<Board> getAllBoards() {
        List<Board> boards = boardRepository.findAllForLaneAllTrue();
        if(Objects.isNull(boards)){
            List<Board> boardsForLanes = boardRepository.findAllByActiveTrue();
            return filterBoardsForLanesTrue(boardsForLanes);
        }
        return boards;
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
        boardForLane.setBoardName(board.getBoardName());
        boardRepository.save(boardForLane);
        return boardForLane;
    }
    @Override
    @Transactional
    public void deleteBoardById(Long boardId) {
        Board board = validateBoardById(boardId);
        board.setActive(false);
        boardRepository.save(board);
        userBoardRepository.deleteByBoard(board);
        //TODO GESTIRE CARATTERI SPECIALI
        //TODO GESTIRE ELIMINAZIONE IN CASCATA DI LANE o TASK
    }
    public Board validateBoardById(Long id) {
        Board existingId = boardRepository.findBoardByBoardIdAndActiveTrue(id);
        if (Objects.isNull(existingId)) {
            throw new ResourceNotFoundException("Error: Board with ID: " + id + " not found.");
        }
        return existingId;
    }
    public void existingBoardById(Long id){
        boolean existingId = boardRepository.existsBoardByBoardIdAndActiveTrue(id);
        if (!existingId) {
            throw new ResourceNotFoundException("Error: Board with ID: " + id + " not found.");
        }
    }
    private Board filterBoardForLanesTrue(Board board){
        List<Lane> lanes = board.getLanes().stream()
                .filter(Lane::isActive)
                .toList();
        board.setLanes(lanes);
        return board;
    }
    private List<Board> filterBoardsForLanesTrue(List<Board> boards){
        return boards.stream()
                .map(this::filterBoardForLanesTrue)
                .toList();
    }
}
