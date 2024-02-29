package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.User;
import com.fincons.taskmanager.entity.UserBoard;
import com.fincons.taskmanager.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserBoardRepository extends JpaRepository<UserBoard, Long> {

    boolean existsByUserAndBoard(User user, Board board);
    UserBoard findByUserEmailAndBoardBoardCode(String email, String boardCode);
}
