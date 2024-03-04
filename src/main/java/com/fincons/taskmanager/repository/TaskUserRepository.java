package com.fincons.taskmanager.repository;


import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.entity.TaskUser;
import com.fincons.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskUserRepository extends JpaRepository <TaskUser, Long> {

    boolean existsByTaskAndUser(Task task, User user);
    TaskUser findByTaskTaskCodeAndUserEmail(String userCode, String email);

    @Query("SELECT tu " +
            "FROM TaskUser tu " +
            "JOIN tu.user u " +
            "WHERE u.email = :userEmail")
    List<TaskUser> findTasksByUser(@Param("userEmail") String userEmail);
}
