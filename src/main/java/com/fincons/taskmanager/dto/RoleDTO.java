package com.fincons.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleDTO {

    private String name;

    //@JsonBackReference
    @JsonIgnoreProperties("roles")
    private List<UserDTO> users;
}
