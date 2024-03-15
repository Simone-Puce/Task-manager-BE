package com.fincons.taskmanager.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserBoardDTO {
    private String email;
    private Long boardId;
    private String boardName;
    private String roleCode;
}
