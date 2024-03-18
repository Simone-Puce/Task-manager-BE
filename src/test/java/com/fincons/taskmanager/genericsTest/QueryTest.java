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

}
