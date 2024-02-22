package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,Long> {

    boolean existsByEmail(String email);
    User findByEmail(String email);

}
