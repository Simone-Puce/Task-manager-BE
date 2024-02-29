package com.fincons.taskmanager.mapper;

import com.fincons.taskmanager.dto.UserBoardDTO;
import com.fincons.taskmanager.entity.BoardLane;
import com.fincons.taskmanager.entity.UserBoard;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserBoardMapper {

    @Autowired
    private ModelMapper modelMapperForUserBoard;

    public UserBoardDTO mapToDTO(UserBoard userBoard){
        return modelMapperForUserBoard.map(userBoard, UserBoardDTO.class);
    }
    public UserBoard mapToEntity(UserBoardDTO userBoardDTO){
        return modelMapperForUserBoard.map(userBoardDTO, UserBoard.class);
    }
}
