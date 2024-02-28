package com.fincons.taskmanager.mapper;

import com.fincons.taskmanager.dto.BoardLaneDTO;
import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.BoardLane;
import com.fincons.taskmanager.entity.Lane;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BoardLaneMapper {
    @Autowired
    private ModelMapper modelMapperStandard;

    public BoardLaneDTO mapBoardLaneToDTO(Board board, Lane lane){
        return new BoardLaneDTO(
                board.getBoardCode(),
                board.getBoardName(),
                lane.getLaneCode(),
                lane.getLaneName()
        );
    }
    public BoardLaneDTO mapToDTO(BoardLane boardLane){
        return modelMapperStandard.map(boardLane, BoardLaneDTO.class);
    }
    public BoardLane mapToEntity(BoardLaneDTO boardLaneDTO){
        return modelMapperStandard.map(boardLaneDTO, BoardLane.class);
    }
}
