package com.fincons.taskmanager.mapperTest;

import com.fincons.taskmanager.dto.*;
import com.fincons.taskmanager.entity.*;
import com.fincons.taskmanager.mapper.BoardMapper;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;

import java.util.ArrayList;
import java.util.List;

public class MapperTest {
    private ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();

        TypeMap<UserBoard, UserDTO> userBoardMapping = modelMapper.createTypeMap(UserBoard.class, UserDTO.class);
        userBoardMapping.addMappings(mapper -> {
            mapper.map(src -> src.getUser().getFirstName(), UserDTO::setFirstName);
            mapper.map(src -> src.getUser().getLastName(), UserDTO::setLastName);
            mapper.map(src -> src.getUser().getEmail(), UserDTO::setEmail);
        });
        return modelMapper;
    }
    @Test
    public void testMapper() {

        User user = new User(3, "user1", "user1", "user1@gmail.com", "12345");
        List<UserBoard> usersBoards = new ArrayList<>();
        Board board = new Board(1L, "C18", "board C18", usersBoards);
        usersBoards.add(new UserBoard(user, board, "ROLE_USER"));


        BoardDTO boardDTO = this.modelMapper().map(board, BoardDTO.class);
        for (UserDTO userDTO : boardDTO.getUsers()) {
            System.out.println(userDTO.getFirstName());
            System.out.println(userDTO.getLastName());
            System.out.println(userDTO.getEmail());
        }
    }
}
