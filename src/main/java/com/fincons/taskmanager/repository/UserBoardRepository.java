package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.User;
import com.fincons.taskmanager.entity.UserBoard;
import com.fincons.taskmanager.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserBoardRepository extends JpaRepository<UserBoard, Long> {

    boolean existsByUserAndBoard(User user, Board board);
    UserBoard findByUserEmailAndBoardBoardId(String email, Long boardId);
    void deleteByBoard(Board board);
    void deleteByUser(User user);
    @Query("SELECT ub " +
            "FROM UserBoard ub " +
            "JOIN ub.user u " +
            "WHERE u.email = :userEmail")
    List<UserBoard> findBoardsByUser(@Param("userEmail") String userEmail);

}
