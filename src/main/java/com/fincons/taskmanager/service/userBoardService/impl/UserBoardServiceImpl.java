package com.fincons.taskmanager.service.userBoardService.impl;

import com.fincons.taskmanager.entity.*;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.exception.RoleException;
import com.fincons.taskmanager.repository.BoardRepository;
import com.fincons.taskmanager.repository.RoleRepository;
import com.fincons.taskmanager.repository.UserBoardRepository;
import com.fincons.taskmanager.repository.UserRepository;
import com.fincons.taskmanager.service.userBoardService.UserBoardService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UserBoardServiceImpl implements UserBoardService {

    private static final Logger log = LogManager.getLogger(UserBoardServiceImpl.class);
    private static final String EDITOR = "EDITOR";
    private static final String ROLEADMIN = "ROLE_ADMIN";
    private static final String ROLEUSER = "ROLE_USER";
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
    public UserBoard createUserBoard(UserBoard userBoard) throws RoleException {

        User existingUser = validateUserByEmail(userBoard.getUser().getEmail());
        Board existingBoard = validateBoardById(userBoard.getBoard().getBoardId());
        checkDuplicateUserBoardExist(existingUser, existingBoard);

        GrantedAuthority authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .findFirst()
                .orElse(null);
        String roleAsString = (authority != null) ? authority.getAuthority() : "is null";
        boolean canUserBoardBeCreated = false;

        if (Objects.equals(roleAsString, ROLEADMIN)) {
            canUserBoardBeCreated = true;
        }

        if (Objects.equals(roleAsString, ROLEUSER)) {
            String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
            List<User> userIsEditor = existingBoard.getUsersBoards().stream()
                    .filter(userBoardToCreate -> EDITOR.equals(userBoardToCreate.getRoleCode()))
                    .map(UserBoard::getUser)
                    .toList();
            boolean isEditorEmailValidated = userIsEditor.stream()
                    .anyMatch(user -> Objects.equals(user.getEmail(), loggedUser));
            if (isEditorEmailValidated) {
                canUserBoardBeCreated = true;
            } else {
                throw new RoleException("You need to be an EDITOR to create a relationship");
            }
        }
        if (canUserBoardBeCreated) {
            UserBoard newUserBoard = new UserBoard(existingUser, existingBoard, userBoard.getRoleCode());
            userBoardRepository.save(newUserBoard);
            return newUserBoard;
        } else {
            log.error("UNEXPECTED ERROR");
            throw new IllegalArgumentException("UNEXPECTED ERROR");
        }
    }

    @Override
    public UserBoard updateUserBoard(String email, Long boardId, UserBoard userBoard) throws RoleException {

        User existingUser = validateUserByEmail(email);
        Board existingBoard = validateBoardById(boardId);
        UserBoard userBoardExist = validateUserBoardRelationship(existingUser, existingBoard);

        User userToUpdate = validateUserByEmail(userBoard.getUser().getEmail());
        Board boardToUpdate = validateBoardById(userBoard.getBoard().getBoardId());

        GrantedAuthority authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .findFirst()
                .orElse(null);
        String roleAsString = (authority != null) ? authority.getAuthority() : "is null";
        boolean canUserBoardBeUpdated = false;

        if (Objects.equals(roleAsString, ROLEADMIN)) {
            canUserBoardBeUpdated = true;
        }

        if (Objects.equals(roleAsString, ROLEUSER)) {
            String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
            List<User> userIsEditor = boardToUpdate.getUsersBoards().stream()
                    .filter(userBoardToFilter -> EDITOR.equals(userBoardToFilter.getRoleCode()))
                    .map(UserBoard::getUser)
                    .toList();
            boolean isEditorEmailValidated = userIsEditor.stream()
                    .anyMatch(user -> Objects.equals(user.getEmail(), loggedUser));
            if (isEditorEmailValidated) {
                canUserBoardBeUpdated = true;
            } else {
                throw new RoleException("You need to be an EDITOR to update a relationship");
            }
        }
        if (canUserBoardBeUpdated) {
            userBoardExist.setUser(userToUpdate);
            userBoardExist.setBoard(boardToUpdate);
            userBoardExist.setRoleCode(userBoard.getRoleCode());
            return userBoardRepository.save(userBoardExist);
        } else {
            log.error("UNEXPECTED ERROR");
            throw new IllegalArgumentException("UNEXPECTED ERROR");
        }
    }

    @Override
    public UserBoard deleteUserBoard(String email, Long boardId) throws RoleException {
        User existingUser = validateUserByEmail(email);
        Board existingBoard = validateBoardById(boardId);
        UserBoard userBoardExist = validateUserBoardRelationship(existingUser, existingBoard);

        GrantedAuthority authority = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .findFirst()
                .orElse(null);
        String roleAsString = (authority != null) ? authority.getAuthority() : "is null";
        boolean canUserBeDeleted = false;

        if (Objects.equals(roleAsString, ROLEADMIN)) {
            canUserBeDeleted = true;
        }

        if (Objects.equals(roleAsString, ROLEUSER)) {
            String loggedUser = SecurityContextHolder.getContext().getAuthentication().getName();
            List<User> userIsEditor = existingBoard.getUsersBoards().stream()
                    .filter(userBoardToFilter -> EDITOR.equals(userBoardToFilter.getRoleCode()))
                    .map(UserBoard::getUser)
                    .toList();
            boolean isEditorEmailValidated = userIsEditor.stream()
                    .anyMatch(user -> Objects.equals(user.getEmail(), loggedUser));
            if (isEditorEmailValidated) {
                canUserBeDeleted = true;
            } else {
                throw new RoleException("You need to be an EDITOR to delete a relationship");
            }
        }
        if (canUserBeDeleted) {
            userBoardRepository.delete(userBoardExist);
            return userBoardExist;
        } else {
            log.error("UNEXPECTED ERROR");
            throw new IllegalArgumentException("UNEXPECTED ERROR");
        }
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
