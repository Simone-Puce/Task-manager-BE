package com.fincons.taskmanager.service.taskUserService;

import com.fincons.taskmanager.entity.TaskUser;

import java.util.List;

public interface TaskUserService {

    List<TaskUser> findTasksByUser(String email);
    TaskUser createTaskUser(TaskUser taskUser);
    TaskUser updateTaskUser(Long taskId, String email, TaskUser taskUser);
    TaskUser deleteTaskUser(Long taskId, String email);
}
