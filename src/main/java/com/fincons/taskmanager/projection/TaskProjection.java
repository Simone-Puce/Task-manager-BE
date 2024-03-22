package com.fincons.taskmanager.projection;

import com.fincons.taskmanager.entity.Lane;
import com.fincons.taskmanager.entity.TaskUser;

import java.util.List;

public interface TaskProjection {
    Long getTaskId();
    String getTaskName();
    String getDescription();
    boolean isActive();
    List<TaskUser> getTasksUsers();
    Lane getLane();
    List<AttachmentProjection> getAttachments();
    String getCreatedBy();
    String getModifiedBy();
    String getCreatedDate();
    String getModifiedDate();
}

