package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    Board findBoardByBoardCode(String code);

    List<Board> findAllByActiveTrue();
}
