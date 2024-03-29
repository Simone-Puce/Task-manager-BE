package com.fincons.taskmanager.mapper;

import com.fincons.taskmanager.dto.RoleDTO;
import com.fincons.taskmanager.dto.UserBoardDTO;
import com.fincons.taskmanager.dto.UserDTO;
import com.fincons.taskmanager.entity.Role;
import com.fincons.taskmanager.entity.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class UserAndRoleMapper {
    private static final ModelMapper modelMapper = new ModelMapper();
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ModelMapper modelMapperForUser;

    public User dtoToUser(UserDTO userDTO) {
        User userToSave = modelMapper.map(userDTO, User.class);
        userToSave.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        return userToSave;
    }

    public UserDTO userToUserDto(User user) {
        UserDTO userDTO = modelMapper.map(user, UserDTO.class);
        userDTO.setRoles(user.getRoles().stream().map(this::roleToRoleDto).toList());
        return userDTO;
    }

    public RoleDTO roleToRoleDto(Role role) {
        return modelMapper.map(role, RoleDTO.class);
    }

    public Role dtoToRole(RoleDTO roleDTO) {
        return modelMapper.map(roleDTO, Role.class);
    }
    public UserDTO mapToDTO(User user) {
        return modelMapperForUser.map(user, UserDTO.class);
    }
    public List<UserDTO> mapEntitiesToDTOs(List<User> users){
        return users.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
