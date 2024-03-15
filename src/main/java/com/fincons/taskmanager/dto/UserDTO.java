package com.fincons.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private List<RoleDTO> roles;
    private List<BoardDTO> boards;
    private List<TaskDTO> tasks;
}
