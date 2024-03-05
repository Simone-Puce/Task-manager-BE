package com.fincons.taskmanager.utilityTest.boardBuilder;

import com.fincons.taskmanager.entity.Board;

public class BoardBuilder {
    public static Board getBoard(){
        return new Board(1L, "boardCode1", "boardName1");
    }
}
