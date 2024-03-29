package com.fincons.taskmanager.service.userBoardService;

import com.fincons.taskmanager.entity.UserBoard;

import java.util.List;

public interface UserBoardService {
    List<UserBoard> findBoardsByUser(String email);
    UserBoard createUserBoard(UserBoard userBoard);
    UserBoard updateUserBoard(String email, Long boardCode, UserBoard userBoard);
    UserBoard deleteUserBoard(String email, Long boardCode);
}
