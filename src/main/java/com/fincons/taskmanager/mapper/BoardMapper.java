package com.fincons.taskmanager.mapper;

import com.fincons.taskmanager.dto.BoardDTO;
import com.fincons.taskmanager.entity.Board;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

}
