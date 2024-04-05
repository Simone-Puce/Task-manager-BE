package com.fincons.taskmanager.service.userBoardService;

import com.fincons.taskmanager.entity.UserBoard;
import com.fincons.taskmanager.exception.RoleException;

import java.util.List;

public interface UserBoardService {
    List<UserBoard> findBoardsByUser(String email);
    UserBoard createUserBoard(UserBoard userBoard) throws RoleException;
    UserBoard updateUserBoard(String email, Long boardCode, UserBoard userBoard) throws RoleException;
    UserBoard deleteUserBoard(String email, Long boardCode) throws RoleException;
}
