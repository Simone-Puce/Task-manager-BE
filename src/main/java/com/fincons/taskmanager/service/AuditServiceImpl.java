package com.fincons.taskmanager.service;

import com.fincons.taskmanager.dto.AuditDTO;
import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.service.boardService.impl.BoardServiceImpl;
import com.fincons.taskmanager.service.taskService.impl.TaskServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditServiceImpl {

    @Autowired
    private BoardServiceImpl boardServiceImpl;

    @Autowired
    private TaskServiceImpl taskServiceImpl;

    public AuditDTO getAudit() {
        List<Board> boards = boardServiceImpl.getAllBoards();
        List<Task> tasks = taskServiceImpl.getAllTasks();
        int numberBoards = boards.size();
        int numberTasks = tasks.size();
        AuditDTO auditDTO = new AuditDTO(numberTasks, numberBoards);
        return auditDTO;
    }

}
