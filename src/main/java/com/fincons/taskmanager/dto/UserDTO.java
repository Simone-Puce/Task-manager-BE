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
public class UserDTO {

    private long id;

    private String firstName;

    private String lastName;

    private String email;

    private String password;
    @JsonIgnoreProperties("users")
    private List<RoleDTO> roles;

    private boolean generatedPassword;


    public void setId(long id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }
}
