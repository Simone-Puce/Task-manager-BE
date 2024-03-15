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
    public List<BoardDTO> mapEntitiesToDTOs(List<Board> boards){
        return boards.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
