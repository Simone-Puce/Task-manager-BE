package com.fincons.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    private String name;

    private List<UserDTO> users;
}
