package com.fincons.taskmanager.service.taskUserService;

import com.fincons.taskmanager.entity.TaskUser;
import com.fincons.taskmanager.exception.RoleException;

import java.util.List;

public interface TaskUserService {

    List<TaskUser> findTasksByUser(String email);
    TaskUser createTaskUser(TaskUser taskUser) throws RoleException;
    TaskUser updateTaskUser(Long taskId, String email, TaskUser taskUser) throws RoleException;
    TaskUser deleteTaskUser(Long taskId, String email) throws RoleException;
}
