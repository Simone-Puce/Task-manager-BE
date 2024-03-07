package com.fincons.taskmanager.utilityTest.taskBuilder;

import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.Task;

import java.util.ArrayList;
import java.util.List;

import static com.fincons.taskmanager.utilityTest.boardBuilder.BoardBuilder.getBoard;

public class TaskBuilder {

    public static Task getTask() {
        return new Task(1L, "taskCode1", "taskName1", "status1", "description1", getBoard());
    }
    public static Task getTaskForModify() {
        return new Task(1L, "taskCode1Put", "taskName1Put", "status1Put", "description1Put", getBoard());
    }

    public static List<Task> getTasks() {

        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, "code1", "task1", "status1", "description1", getBoard()));
        tasks.add(new Task(2L, "code2", "task2", "status2", "description2", getBoard()));
        tasks.add(new Task(3L, "code3", "task3", "status3", "description3", getBoard()));
        return tasks;
    }
    public static Task getTaskWithoutId() {
        return new Task(1L, "code1", "name1", "status1", "description1", getBoard());
    }
    public static List<Task> getTasksEmpty() {
        return new ArrayList<>();
    }
}
