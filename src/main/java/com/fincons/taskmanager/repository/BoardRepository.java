package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {

    @Query(value = "SELECT b " +
            "FROM Board b " +
            "JOIN FETCH b.boardsLanes bl " +
            "WHERE b.boardId = :boardId " +
            "AND b.active = true " +
            "AND bl.lane.active = true "
    )
    Board findBoardIdForLaneAllTrue(@Param("boardId")Long boardId);

    @Query(value = "SELECT b " +
            "FROM Board b " +
            "JOIN FETCH b.tasks t " +
            "WHERE b.boardId = :boardId " +
            "AND b.active = true " +
            "AND t.active = true "
    )
    Board findBoardIdForTaskAllTrue(@Param("boardId")Long boardId);

    @Query(value = "SELECT b " +
            "FROM Board b " +
            "JOIN FETCH b.boardsLanes bl " +
            "WHERE b.active = true " +
            "AND bl.lane.active = true "
    )
    List<Board> findAllForLaneAllTrue();
    @Query(value = "SELECT b " +
            "FROM Board b " +
            "JOIN FETCH b.tasks t " +
            "WHERE b.active = true " +
            "AND t.active = true "
    )
    List<Board> findAllForTaskAllTrue();
    Board findBoardByBoardIdAndActiveTrue(Long id);
    boolean existsBoardByBoardIdAndActiveTrue(Long id);
}