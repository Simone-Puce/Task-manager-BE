package com.fincons.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BoardDTO {

    private String boardCode;
    private String boardName;
    private List<LaneDTO> lanes;
    private List<TaskDTO> tasks;
    private List<UserDTO> users;
    private String createdBy;
    private String modifiedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
    private Timestamp createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
    private Timestamp modifiedDate;
    private boolean active;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BoardDTO boardDTO = (BoardDTO) o;
        return Objects.equals(boardCode, boardDTO.boardCode) && Objects.equals(boardName, boardDTO.boardName) && Objects.equals(lanes, boardDTO.lanes) && Objects.equals(tasks, boardDTO.tasks) && Objects.equals(users, boardDTO.users) && Objects.equals(createdBy, boardDTO.createdBy) && Objects.equals(modifiedBy, boardDTO.modifiedBy) && Objects.equals(createdDate, boardDTO.createdDate) && Objects.equals(modifiedDate, boardDTO.modifiedDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(boardCode, boardName, lanes, tasks, users, createdBy, modifiedBy, createdDate, modifiedDate);
    }
}
