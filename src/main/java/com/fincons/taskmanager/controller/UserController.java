package com.fincons.taskmanager.controller;

import com.fincons.taskmanager.dto.LoginDTO;
import com.fincons.taskmanager.dto.UserDTO;
import com.fincons.taskmanager.exception.EmailException;
import com.fincons.taskmanager.exception.PasswordException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.exception.RoleException;
import com.fincons.taskmanager.jwt.JwtAuthResponse;
import com.fincons.taskmanager.mapper.UserAndRoleMapper;
import com.fincons.taskmanager.service.authService.UserService;
import com.fincons.taskmanager.utility.GenericResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/task-manager")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    UserAndRoleMapper userAndRoleMapper;
    @GetMapping("${error.base.uri}")
    public String errorEndpoint() {
        return "Error!";
    }

    @PostMapping("${login.base.uri}")
    public ResponseEntity<GenericResponse<JwtAuthResponse>> login(@RequestBody LoginDTO loginDto) {
        try {
            String token = userService.login(loginDto);
            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
            jwtAuthResponse.setAccessToken(token);
            return ResponseEntity.ok(GenericResponse.<JwtAuthResponse>builder()
                    .status(HttpStatus.OK)
                    .success(true)
                    .message("Logged successfully!!!")
                    .data(jwtAuthResponse)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(GenericResponse.<JwtAuthResponse>builder()
                    .status(HttpStatus.CONFLICT)
                    .success(false)
                    .message(e.getMessage())
                    .build());
        }
    }

    @GetMapping("${registered.users}")
    public ResponseEntity<GenericResponse<List<UserDTO>>> registeredUsers() {

        List<UserDTO> userDTOList = userService.findAllUsers()
                .stream()
                .map(user -> userAndRoleMapper.userToUserDto(user))
                .toList();
        if (!userDTOList.isEmpty()) {
            return ResponseEntity.ok(GenericResponse.<List<UserDTO>>builder()
                    .status(HttpStatus.OK)
                    .success(true)
                    .message("List of  registered users")
                    .data(userDTOList)
                    .build());
        } else {
            return ResponseEntity.ok(GenericResponse.<List<UserDTO>>builder()
                    .status(HttpStatus.OK)
                    .success(true)
                    .message("List is empty")
                    .data(userDTOList)
                    .build());
        }
    }

    @PostMapping(value = "${register.base.uri}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GenericResponse<UserDTO>> register(
            @RequestBody UserDTO userDTO,
            @RequestParam(name = "admin", required = false) String passwordForAdmin) {
        try {
            UserDTO registeredUser = userAndRoleMapper.mapToDTO(userService.registerNewUser(userDTO, passwordForAdmin));
            return ResponseEntity.ok(GenericResponse.<UserDTO>builder()
                    .status(HttpStatus.OK)
                    .success(true)
                    .message("Registered successfully!!!")
                    .data(registeredUser)
                    .build());

        } catch (EmailException ee) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(GenericResponse.<UserDTO>builder()
                    .status(HttpStatus.CONFLICT)
                    .success(false)
                    .message(EmailException.emailInvalidOrExist())
                    .build());

        } catch (PasswordException passwordEx) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(GenericResponse.<UserDTO>builder()
                    .status(HttpStatus.CONFLICT)
                    .success(false)
                    .message(passwordEx.getMessage())
                    .build());
        }
    }

    @PutMapping("${modify.user}")
    public ResponseEntity<GenericResponse<UserDTO>> updateUserByEmail
            (
                    @RequestParam(name = "email") String email,
                    @RequestBody UserDTO userModified,
                    @RequestParam(name = "admin", required = false) String passwordForAdmin
            ) throws Exception {
        try {
            UserDTO updatedUser = userAndRoleMapper.userToUserDto(userService.updateUser(email, userModified, passwordForAdmin));
            return ResponseEntity.status(HttpStatus.OK).body(
                    GenericResponse.<UserDTO>builder()
                            .status(HttpStatus.OK)
                            .success(true)
                            .message("User modified successfully!")
                            .data(updatedUser).build()
            );
        } catch (RoleException roleException) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                    GenericResponse.<UserDTO>builder()
                            .status(HttpStatus.FORBIDDEN)
                            .success(false)
                            .message(roleException.getMessage())
                            .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.<UserDTO>builder()
                            .status(HttpStatus.NOT_FOUND)
                            .success(false)
                            .message(resourceNotFoundException.getMessage())
                            .build()
            );
        } catch (PasswordException passwordException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    GenericResponse.<UserDTO>builder()
                            .status(HttpStatus.CONFLICT)
                            .success(false)
                            .message(passwordException.getMessage())
                            .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    GenericResponse.<UserDTO>builder()
                            .status(HttpStatus.CONFLICT)
                            .success(false)
                            .message(e.getMessage())
                            .build()
            );
        }
    }

    @PutMapping("${update.user.password}")
    public ResponseEntity<GenericResponse<UserDTO>> updateUserPassword(
            @RequestParam String email,
            @RequestParam String currentPassword,
            @RequestParam String newPassword) {
        try {
            UserDTO userDTO = userAndRoleMapper.userToUserDto(userService.updateUserPassword(email, currentPassword, newPassword));
            return ResponseEntity.status(HttpStatus.OK).body(
                    GenericResponse.<UserDTO>builder()
                            .status(HttpStatus.OK)
                            .success(true)
                            .message("Password changed successfully")
                            .data(userDTO)
                            .build()
            );
        } catch (EmailException | PasswordException emailOrPasswordException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    GenericResponse.<UserDTO>builder()
                            .status(HttpStatus.CONFLICT)
                            .success(false)
                            .message(emailOrPasswordException.getMessage()).build());
        }
    }

    @GetMapping("${detail.userdto}")
    public ResponseEntity<GenericResponse<UserDTO>> getUserByEmail() {
        try {
            UserDTO userDTO = userAndRoleMapper.mapToDTO(userService.getUserDetails());
            return ResponseEntity.status(HttpStatus.OK).body(
                    GenericResponse.<UserDTO>builder()
                            .status(HttpStatus.OK)
                            .success(true)
                            .message("User found!")
                            .data(userDTO)
                            .build());
        } catch (ResourceNotFoundException resourceNotFoundException) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.<UserDTO>builder()
                            .status(HttpStatus.NOT_FOUND)
                            .success(false)
                            .message(resourceNotFoundException.getMessage())
                            .build());
        }
    }

    @DeleteMapping("${delete.user-by-email}")
    public ResponseEntity<GenericResponse<Boolean>> deleteUserByEmail(@RequestParam String email) {
        try {
            userService.deleteUserByEmail(email);
            return ResponseEntity.status(HttpStatus.OK).body(
                    GenericResponse.<Boolean>builder()
                            .status(HttpStatus.OK)
                            .success(true)
                            .message("User deleted successfully!!")
                            .build());
        } catch (EmailException emailException) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    GenericResponse.<Boolean>builder()
                            .status(HttpStatus.CONFLICT)
                            .success(false)
                            .message(emailException.getMessage())
                            .build());
        } catch (RoleException roleException) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                    GenericResponse.<Boolean>builder()
                            .status(HttpStatus.UNAUTHORIZED)
                            .success(false)
                            .message(roleException.getMessage())
                            .build());
        }
    }
}
