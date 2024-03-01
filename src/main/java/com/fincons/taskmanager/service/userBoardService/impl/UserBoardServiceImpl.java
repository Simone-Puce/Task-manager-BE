package com.fincons.taskmanager.service.userBoardService.impl;

import com.fincons.taskmanager.entity.*;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.BoardRepository;
import com.fincons.taskmanager.repository.RoleRepository;
import com.fincons.taskmanager.repository.UserBoardRepository;
import com.fincons.taskmanager.repository.UserRepository;
import com.fincons.taskmanager.service.userBoardService.UserBoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserBoardServiceImpl implements UserBoardService {

    @Autowired
    private UserBoardRepository userBoardRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    public List<UserBoard> findBoardsByUser(String email) {
        validateUserByEmail(email);
        return userBoardRepository.findBoardsByUser(email);
    }

    @Override
    public UserBoard createUserBoard(UserBoard userBoard) {

        User existingUser = validateUserByEmail(userBoard.getUser().getEmail());
        Board existingBoard = validateBoardByCode(userBoard.getBoard().getBoardCode());
        Role existingRole = validateRoleByName(userBoard.getRoleCode());

        checkDuplicateUserBoardExist(existingUser, existingBoard);
        UserBoard newUserBoard = new UserBoard(existingUser, existingBoard, existingRole.getName());
        userBoardRepository.save(newUserBoard);
        return newUserBoard;
    }
    @Override
    public UserBoard updateUserBoard(String email, String boardCode, UserBoard userBoard) {

        User existingUser = validateUserByEmail(email);
        Board existingBoard = validateBoardByCode(boardCode);
        Role existingRole = validateRoleByName(userBoard.getRoleCode());
        UserBoard userBoardExist = validateUserBoardRelationship(existingUser, existingBoard);

        User userToUpdate = validateUserByEmail(userBoard.getUser().getEmail());
        Board boardToUpdate = validateBoardByCode(userBoard.getBoard().getBoardCode());
        validateUserBoardNotExistRelationship(userToUpdate, boardToUpdate);

        userBoardExist.setUser(userToUpdate);
        userBoardExist.setBoard(boardToUpdate);
        userBoardExist.setRoleCode(existingRole.getName());
        userBoardRepository.save(userBoardExist);

        return userBoardExist;
    }
    @Override
    public UserBoard deleteUserBoard(String email, String boardCode) {
        User existingUser = validateUserByEmail(email);
        Board existingBoard = validateBoardByCode(boardCode);
        UserBoard userBoardExist = validateUserBoardRelationship(existingUser, existingBoard);
        userBoardRepository.delete(userBoardExist);
        return userBoardExist;
    }
    private User validateUserByEmail(String email) {
        User existingUser = userRepository.findByEmail(email);
        if (Objects.isNull(existingUser)) {
            throw new ResourceNotFoundException("Error: User with EMAIL: " + email + " not found.");
        }
        return existingUser;
    }
    private Board validateBoardByCode(String code) {
        Board existingBoard = boardRepository.findBoardByBoardCode(code);
        if (Objects.isNull(existingBoard)) {
            throw new ResourceNotFoundException("Error: Board with CODE: " + code + " not found.");
        }
        return existingBoard;
    }
    private Role validateRoleByName(String name){
        Role existingRole = roleRepository.findByName(name);
        if(Objects.isNull(existingRole)){
            throw new ResourceNotFoundException("Error: Role with NAME: " + name + " not found.");
        }
        return existingRole;
    }
    private void checkDuplicateUserBoardExist(User user, Board board) {

        boolean userBoardExist = userBoardRepository.existsByUserAndBoard(user, board);
        if (userBoardExist) {
            throw new DuplicateException(
                    "USER: " + user.getEmail() + " and BOARD: " + board.getBoardCode(),
                    "USER: " + user.getEmail() + " and BOARD: " + board.getBoardCode());
        }
    }
    private UserBoard validateUserBoardRelationship(User user, Board board) {
        UserBoard userBoardExist = userBoardRepository.findByUserEmailAndBoardBoardCode(user.getEmail(), board.getBoardCode());
        if (Objects.isNull(userBoardExist)) {
            throw new ResourceNotFoundException("Error: Relationship with EMAIL : " + user.getEmail() +
                    " and CODE board: " + board.getBoardCode() + " don't exists.");
        }
        return userBoardExist;
    }
    private void validateUserBoardNotExistRelationship(User user, Board board) {
        UserBoard userBoardExist = userBoardRepository.findByUserEmailAndBoardBoardCode(user.getEmail(), board.getBoardCode());
        if (!Objects.isNull(userBoardExist)) {
            throw new ResourceNotFoundException("Error: Relationship with EMAIL: " + user.getEmail() +
                    " and CODE board: " + board.getBoardCode() + " already exist.");
        }
    }
}
