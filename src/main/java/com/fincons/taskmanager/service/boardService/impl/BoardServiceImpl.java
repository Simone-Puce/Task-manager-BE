package com.fincons.taskmanager.service.boardService.impl;

import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.Lane;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.*;
import com.fincons.taskmanager.service.boardService.BoardService;
import com.fincons.taskmanager.utility.builder.LaneBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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
    @Autowired
    private TaskUserRepository taskUserRepository;
    @Autowired
    private AttachmentRepository attachmentRepository;
    private static final Logger log = LogManager.getLogger(BoardServiceImpl.class);

    @Override
    public Board getBoardById(Long boardId) {
        existingBoardById(boardId);
        return filterTasksInBoard(filterBoardForLanesTrue(boardRepository.findBoardByBoardIdAndActiveTrue(boardId)));
    }
    @Override
    public List<Board> getAllBoards() {
        return sortBoardsList(filterTasksInBoards(filterBoardsForLanesTrue(boardRepository.findAllByActiveTrue())));
    }

    @Override
    public Board createBoard(Board board) {
        board.setActive(true);
        boardRepository.save(board);
        List<Lane> lanes = saveLanes(board);
        board.setLanes(lanes);
        log.info("New Board saved in the repository with ID {}.", board.getBoardId());
        return board;
    }
    @Override
    public Board updateBoardById(Long boardId, Board board) {
        existingBoardById(boardId);
        Board boardExisting = boardRepository.findBoardByBoardIdAndActiveTrue(boardId);
        boardExisting.setBoardName(board.getBoardName());
        boardRepository.save(boardExisting);
        log.info("Updated Board in the repository with ID {}.", boardExisting.getBoardId());
        return filterBoardForLanesTrue(boardExisting);
    }
    @Override
    @Transactional
    public void deleteBoardById(Long boardId) {
        Board board = validateBoardById(boardId);
        board.setActive(false);
        boardRepository.save(board);
        userBoardRepository.deleteByBoard(board);
        board.getLanes().forEach(lane -> {
            log.info("Lane with ID {} deleted from the repository.", lane.getLaneId());
            lane.setActive(false);
            lane.getTasks().forEach(task -> {
                log.info("Task with ID {} deleted from the repository.", task.getTaskId());
                taskUserRepository.deleteByTask(task);
                task.setActive(false);
                task.getAttachments().forEach(attachment -> {
                    log.info("Attachment with ID {} deleted from the repository.", attachment.getAttachmentId());
                    attachmentRepository.delete(attachment);
                });
            });
        });
        log.info("Board with ID {} deleted from the repository.", board.getBoardId());
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
        log.info("Board retrieved from repository with ID: {} exists", id);
    }
    private Board filterBoardForLanesTrue(Board board){
        List<Lane> lanes = board.getLanes().stream()
                .filter(Lane::isActive)
                .toList();
        board.setLanes(lanes);
        return board;
    }
    private Board filterTasksInBoard(Board board) {
        List<Lane> lanesInBoard = board.getLanes();
        lanesInBoard.forEach(lane -> {
            List<Task> taskList = lane.getTasks();
            List<Task> filterTaskList = taskList.stream().filter(Task::isActive).toList();
            lane.setTasks(filterTaskList);
        });
        board.setLanes(lanesInBoard);
        return board;
    }

    private List<Board> filterTasksInBoards(List<Board> boards){
        return boards.stream()
                .map(this::filterTasksInBoard)
                .toList();
    }
    private List<Board> filterBoardsForLanesTrue(List<Board> boards){
        return boards.stream()
                .map(this::filterBoardForLanesTrue)
                .toList();
    }
    private List<Board> sortBoardsList(List<Board> boards){
        List<Board> sortedBoards = new ArrayList<>(boards);
        sortedBoards.sort(Comparator.comparing(Board::getBoardName)
                .thenComparing(Board::getCreatedDate));
        return sortedBoards;
    }
    private List<Lane> saveLanes(Board board){
        List<Lane> lanesDefault = LaneBuilder.getDefaultListLane();
        for (Lane lane : lanesDefault) {
            lane.setBoard(board);
            lane.setActive(true);
            laneRepository.save(lane);
            log.info("New Lane saved in the repository with ID {}.", lane.getLaneId());
        }
        return lanesDefault;
    }
}
