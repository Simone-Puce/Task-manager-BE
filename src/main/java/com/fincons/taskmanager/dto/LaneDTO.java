package com.fincons.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fincons.taskmanager.entity.Board;
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
public class LaneDTO {

    private String laneCode;
    private String name;
    private List<Board> boards;
}
