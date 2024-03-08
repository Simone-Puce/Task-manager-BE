package com.fincons.taskmanager.mapper;

import com.fincons.taskmanager.dto.BoardLaneDTO;
import com.fincons.taskmanager.entity.BoardLane;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class BoardLaneMapper {
    @Autowired
    private ModelMapper modelMapperForBoardLane;
    public BoardLaneDTO mapToDTO(BoardLane boardLane){
        return modelMapperForBoardLane.map(boardLane, BoardLaneDTO.class);
    }

    public List<BoardLaneDTO> mapEntitiesToDTOs(List<BoardLane> boardsLanes){
        return boardsLanes.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    public BoardLane mapToEntity(BoardLaneDTO boardLaneDTO){
        return modelMapperForBoardLane.map(boardLaneDTO, BoardLane.class);
    }
}
