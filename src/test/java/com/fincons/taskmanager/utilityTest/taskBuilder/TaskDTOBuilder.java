package com.fincons.taskmanager.utilityTest.taskBuilder;

import com.fincons.taskmanager.dto.TaskDTO;
import com.fincons.taskmanager.entity.Task;

import static com.fincons.taskmanager.utilityTest.boardBuilder.BoardBuilder.getBoard;

public class TaskDTOBuilder {

    public static TaskDTO getTaskDTO(){
        return new TaskDTO("taskCode1", "taskName1", "status1", "description1", "boardCode");
    }
    public static TaskDTO getTaskDTOForModify() {
        return new TaskDTO("taskCode1Put", "taskName1Put", "status1Put", "description1Put", "boardCodePut");
    }
    public static TaskDTO getTaskDTOWithoutFieldCode(){
        return new TaskDTO("", "taskName1", "status1", "description1", "boardCode");
    }
    public static TaskDTO getTaskDTOWithoutOtherFieldCannotBeNull(){
        return new TaskDTO("", "", "", "description1", "boardCode");
    }
}
