package com.fincons.taskmanager.genericsTest;

import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.repository.BoardRepository;
import com.fincons.taskmanager.service.boardService.impl.BoardServiceImpl;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class QueryTest {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private BoardServiceImpl boardService;
    @Autowired
    private BoardRepository boardRepository;

    @Test
    public void queryTest(){
        String jpql = "SELECT DISTINCT b " +
                "FROM Board b " +
                "LEFT JOIN FETCH b.tasks t " +
                "LEFT JOIN FETCH b.boardsLanes bl " +
                "LEFT JOIN FETCH bl.lane l " +
                "WHERE b.boardId = :boardId AND b.active = true AND t.active = true AND bl.active = false AND l.active = true ";


        Board board = entityManager.createQuery(jpql, Board.class)
                .setParameter("boardId", 2)
                .getSingleResult();

        System.out.println(board.getTasks().size());
        assertNotNull(board);
        assertNotNull(board.getTasks());
        assertFalse(board.getTasks().isEmpty());
    }
}
