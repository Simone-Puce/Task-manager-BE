package com.fincons.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    private String boardCode;
    private String name;
    private List<LaneDTO> lanes;
    private List<TaskDTO> tasks;
    private List<UserDTO> users;
    private long createdDate;
    private long modifiedDate;
}
