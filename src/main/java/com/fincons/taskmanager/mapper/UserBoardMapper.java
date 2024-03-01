package com.fincons.taskmanager.mapper;

import com.fincons.taskmanager.dto.UserBoardDTO;
import com.fincons.taskmanager.entity.BoardLane;
import com.fincons.taskmanager.entity.UserBoard;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserBoardMapper {

    @Autowired
    private ModelMapper modelMapperForUserBoard;

    public UserBoardDTO mapToDTO(UserBoard userBoard){
        return modelMapperForUserBoard.map(userBoard, UserBoardDTO.class);
    }
    public List<UserBoardDTO> mapEntitiesToDTOs(List<UserBoard> usersBoards){
        return usersBoards.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
    public UserBoard mapToEntity(UserBoardDTO userBoardDTO){
        return modelMapperForUserBoard.map(userBoardDTO, UserBoard.class);
    }
}
