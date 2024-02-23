package com.fincons.taskmanager.utilityTest;

import com.fincons.taskmanager.entity.Task;

import java.util.ArrayList;
import java.util.List;

public class TaskBuilder {
    public static List<Task> getTasks() {
        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, "code1", "name1", "status1", "description1"));
        tasks.add(new Task(2L, "code2", "name2", "status2", "description2"));
        tasks.add(new Task(3L, "code3", "name3", "status3", "description3"));
        return tasks;
    }
    public static Task getTask() {
        return new Task(3L, "code1", "name1", "status1", "description1");
    }
    public static Task getTaskWithoutId() {
        return new Task("code1", "name1", "status1", "description1");
    }
    public static List<Task> getTasksEmpty() {
        return new ArrayList<>();
    }
}
