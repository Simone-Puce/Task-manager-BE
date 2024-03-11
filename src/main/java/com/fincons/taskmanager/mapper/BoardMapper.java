package com.fincons.taskmanager.mapper;

import com.fincons.taskmanager.dto.BoardDTO;
import com.fincons.taskmanager.dto.LaneDTO;
import com.fincons.taskmanager.dto.TaskDTO;
import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.Task;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BoardMapper {
    @Autowired
    private ModelMapper modelMapperForBoard;

    public BoardDTO mapToDTO(Board board) {
        return modelMapperForBoard.map(board, BoardDTO.class);
    }

    public Board mapToEntity(BoardDTO boardDTO){
        return modelMapperForBoard.map(boardDTO, Board.class);
    }
    public BoardDTO mapToDTOOnlyActive(Board board) {
        BoardDTO boardDTO = modelMapperForBoard.map(board, BoardDTO.class);
        List<TaskDTO> activeTasks = board.getTasks().stream()
                .filter(Task::isActive)
                .map(task -> modelMapperForBoard.map(task, TaskDTO.class))
                .collect(Collectors.toList());
        boardDTO.setTasks(activeTasks);
        List<LaneDTO> activeLanes = board.getBoardsLanes().stream()
                .filter(boardLane -> boardLane.getLane().isActive())
                .map(lane -> modelMapperForBoard.map(lane, LaneDTO.class))
                .collect(Collectors.toList());
        boardDTO.setLanes(activeLanes);
        return boardDTO;
    }
    public List<BoardDTO> mapEntitiesToDTOsOnlyActive(List<Board> boards){
        return boards.stream()
                .map(board -> {
                    BoardDTO boardDTO = modelMapperForBoard.map(board, BoardDTO.class);
                    List<TaskDTO> filteredTasks = board.getTasks().stream()
                            .filter(Task::isActive)
                            .map(task -> modelMapperForBoard.map(task, TaskDTO.class))
                            .collect(Collectors.toList());
                    boardDTO.setTasks(filteredTasks);
                    return boardDTO;
                })
                .map(board -> {
                    BoardDTO boardDTO = modelMapperForBoard.map(board, BoardDTO.class);
                    List<LaneDTO> filteredLanes = board.getLanes().stream()
                            .filter(LaneDTO::isActive)
                            .map(lane -> modelMapperForBoard.map(lane, LaneDTO.class))
                            .collect(Collectors.toList());
                    boardDTO.setLanes(filteredLanes);
                    return boardDTO;
                })
                .collect(Collectors.toList());
    }
}
