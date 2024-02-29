package com.fincons.taskmanager.service.userBoardService;

import com.fincons.taskmanager.entity.UserBoard;

public interface UserBoardService {
    UserBoard createUserBoard(UserBoard userBoard);
    UserBoard updateUserBoard(String email, String boardCode, UserBoard userBoard);
    UserBoard deleteUserBoard(String email, String boardCode);
}
