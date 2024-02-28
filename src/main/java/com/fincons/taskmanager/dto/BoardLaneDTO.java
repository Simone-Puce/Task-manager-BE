package com.fincons.taskmanager.dto;

import com.fincons.taskmanager.entity.Lane;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

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
