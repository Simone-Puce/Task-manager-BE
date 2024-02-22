package com.fincons.taskmanager.service.authService;

import com.fincons.taskmanager.dto.LoginDTO;
import com.fincons.taskmanager.dto.UserDTO;
import com.fincons.taskmanager.entity.User;
import com.fincons.taskmanager.exception.EmailException;
import com.fincons.taskmanager.exception.PasswordException;
import com.fincons.taskmanager.exception.RoleException;

import java.util.List;

public interface UserService {
    User registerNewUser(UserDTO newUserDTO, String passwordForAdmin, String passwordForUser) throws EmailException, PasswordException;

    User getUserDtoByEmail(String email);

    String login(LoginDTO loginDto);

    User updateUser(String email, UserDTO userModified, String passwordForAdmin) throws Exception;

    User updateUserPassword(String email, String currentPassword, String newPassword) throws EmailException, PasswordException;

    List<User> findAllUsers();

    void deleteUserByEmail(String email) throws EmailException, RoleException;
}
