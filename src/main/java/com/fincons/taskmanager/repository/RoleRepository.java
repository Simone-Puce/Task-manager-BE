package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository  extends JpaRepository<Role,Long> {
    Role findByName(String roleUser);
    boolean existsByName(String name);
}
