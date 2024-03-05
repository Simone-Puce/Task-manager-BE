package com.fincons.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TaskDTO {

    private String taskCode;
    private String taskName;
    private String status;
    private String description;
    private List<UserDTO> users;
    private String boardCode;
    private List<AttachmentDTO> attachments;
    private String createdBy;
    private String modifiedBy;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
    private Timestamp createdDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM-dd-yyyy HH:mm:ss")
    private Timestamp modifiedDate;


    public TaskDTO(String taskCode, String taskName, String status, String description, String boardCode) {
        this.taskCode = taskCode;
        this.taskName = taskName;
        this.status = status;
        this.description = description;
        this.boardCode = boardCode;
    }

}
