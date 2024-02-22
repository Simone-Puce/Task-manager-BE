package com.fincons.taskmanager.service.authService;

import com.fincons.taskmanager.dto.RoleDTO;
import com.fincons.taskmanager.entity.Role;
import com.fincons.taskmanager.exception.RoleException;

import java.util.List;

public interface RoleService {
    Role getRoleById(long roleId);

    Role createRole(RoleDTO roleDTO) throws RoleException;

    Role updateRole(long roleId, RoleDTO roleModifiedDTO) throws RoleException;

    String deleteRole(long roleId, boolean deleteUsers) throws RoleException;

    List<Role> findAllRoles();
}