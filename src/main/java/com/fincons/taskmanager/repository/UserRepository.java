package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT u " +
            "FROM User u " +
            "LEFT JOIN FETCH u.usersBoards ub " +
            "LEFT JOIN FETCH u.tasksUsers tu " +
            "WHERE (ub.board.active = true OR ub.board IS NULL) " +
            "AND (tu.task.active = true OR tu.task IS NULL)"
    )
    List<User> findAllUsersByBoardAndTaskActive();

    @Query(value = "SELECT u " +
            "FROM User u " +
            "JOIN u.roles r " +
            "WHERE r.name = 'ROLE_USER' ")
    List<User> findAllUserByROLE_USER();
    boolean existsByEmail(String email);

    User findByEmail(String email);

}
