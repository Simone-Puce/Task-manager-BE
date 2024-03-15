package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {


    @Query(value = "SELECT b " +
            "FROM Board b " +
            "LEFT JOIN FETCH b.lanes l " +
            "WHERE b.boardId = :boardId " +
            "AND b.active = true " +
            "AND (l.active = true OR l IS NULL) "
    )
    Board findBoardIdForLaneAllTrue(@Param("boardId")Long boardId);

    @Query(value = "SELECT b " +
            "FROM Board b " +
            "JOIN FETCH b.lanes l " +
            "WHERE b.active = true " +
            "AND (l.active = true OR l IS NULL)"
    )
    List<Board> findAllForLaneAllTrue();
    Board findBoardByBoardIdAndActiveTrue(Long id);
    List<Board> findAllByActiveTrue();
    boolean existsBoardByBoardIdAndActiveTrue(Long id);
}