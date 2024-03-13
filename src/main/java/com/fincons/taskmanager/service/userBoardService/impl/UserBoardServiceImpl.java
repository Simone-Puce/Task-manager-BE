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
        Board existingBoard = validateBoardById(userBoard.getBoard().getBoardId());

        checkDuplicateUserBoardExist(existingUser, existingBoard);
        UserBoard newUserBoard = new UserBoard(existingUser, existingBoard, userBoard.getRoleCode());
        userBoardRepository.save(newUserBoard);

        return newUserBoard;
    }

    @Override
    public UserBoard updateUserBoard(String email, Long boardId, UserBoard userBoard) {

        User existingUser = validateUserByEmail(email);
        Board existingBoard = validateBoardById(boardId);
        UserBoard userBoardExist = validateUserBoardRelationship(existingUser, existingBoard);

        User userToUpdate = validateUserByEmail(userBoard.getUser().getEmail());
        Board boardToUpdate = validateBoardById(userBoard.getBoard().getBoardId());
        validateUserBoardNotExistRelationship(userToUpdate, boardToUpdate);

        userBoardExist.setUser(userToUpdate);
        userBoardExist.setBoard(boardToUpdate);
        userBoardExist.setRoleCode(userBoard.getRoleCode());
        userBoardRepository.save(userBoardExist);

        return userBoardExist;

    }
    @Override
    public UserBoard deleteUserBoard(String email, Long boardId) {
        User existingUser = validateUserByEmail(email);
        Board existingBoard = validateBoardById(boardId);
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
    private Board validateBoardById(Long id) {
        Board existingBoard = boardRepository.findBoardByBoardIdAndActiveTrue(id);
        if (Objects.isNull(existingBoard)) {
            throw new ResourceNotFoundException("Error: Board with ID: " + id + " not found.");
        }
        return existingBoard;
    }
    private void checkDuplicateUserBoardExist(User user, Board board) {

        boolean userBoardExist = userBoardRepository.existsByUserAndBoard(user, board);
        if (userBoardExist) {
            throw new DuplicateException(
                    "USER: " + user.getEmail() + " and BOARD: " + board.getBoardId(),
                    "USER: " + user.getEmail() + " and BOARD: " + board.getBoardId());
        }
    }
    private UserBoard validateUserBoardRelationship(User user, Board board) {
        UserBoard userBoardExist = userBoardRepository.findByUserEmailAndBoardBoardId(user.getEmail(), board.getBoardId());
        if (Objects.isNull(userBoardExist)) {
            throw new ResourceNotFoundException("Error: Relationship with EMAIL : " + user.getEmail() +
                    " and ID board: " + board.getBoardId() + " don't exists.");
        }
        return userBoardExist;
    }
    private void validateUserBoardNotExistRelationship(User user, Board board) {
        UserBoard userBoardExist = userBoardRepository.findByUserEmailAndBoardBoardId(user.getEmail(), board.getBoardId());
        if (!Objects.isNull(userBoardExist)) {
            throw new ResourceNotFoundException("Error: Relationship with EMAIL: " + user.getEmail() +
                    " and ID board: " + board.getBoardId() + " already exist.");
        }
    }
}
