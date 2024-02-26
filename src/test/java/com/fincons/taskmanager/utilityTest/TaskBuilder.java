package com.fincons.taskmanager.utilityTest;

import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.entity.User;

import java.util.ArrayList;
import java.util.List;

public class TaskBuilder {

    public static Board getBoard(){
        Board board = new Board(1L, "code1", "name1");
        return board;
    }

    public static List<Task> getTasks() {

        List<Task> tasks = new ArrayList<>();
        tasks.add(new Task(1L, "code1", "task1", "status1", "description1", getBoard()));
        tasks.add(new Task(2L, "code2", "task2", "status2", "description2", getBoard()));
        tasks.add(new Task(3L, "code3", "task3", "status3", "description3", getBoard()));
        return tasks;
    }
    public static Task getTask() {
        Board board = new Board(1L, "code1", "name1");
        return new Task(3L, "code1", "name1", "status1", "description1", getBoard());
    }
    public static Task getTaskWithoutId() {
        Board board = new Board(1L, "code1", "name1");
        return new Task(1L, "code1", "name1", "status1", "description1", board);
    }
    public static List<Task> getTasksEmpty() {
        return new ArrayList<>();
    }
}
