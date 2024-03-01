package com.fincons.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskUserDTO {
    private String taskCode;
    private String taskName;
    private String firstName;
    private String lastName;
    private String email;
}
