package com.fincons.taskmanager.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardLaneDTO {

    private String boardCode;
    private String boardName;
    private String laneCode;
    private String laneName;
}
