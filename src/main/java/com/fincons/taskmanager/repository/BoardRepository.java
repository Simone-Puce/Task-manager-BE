package com.fincons.taskmanager.repository;


import com.fincons.taskmanager.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Board findBoardByBoardCode(String code);
}
