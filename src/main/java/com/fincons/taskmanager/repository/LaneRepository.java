package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.Lane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LaneRepository extends JpaRepository<Lane, Long> {
    @Query(value = "SELECT l " +
                    "FROM Lane l " +
                    "JOIN FETCH l.boardsLanes bl " +
                    "WHERE l.laneId = :laneId " +
                    "AND l.active = true " +
                    "AND bl.board.active = true "
    )
    Lane findLaneIdForBoardsAllTrue(@Param("laneId")Long laneId);
    @Query(value = "SELECT l " +
            "FROM Lane l " +
            "JOIN FETCH l.boardsLanes bl " +
            "WHERE l.active = true " +
            "AND bl.board.active = true "
    )
    List<Lane> findAllForBoardsAllTrue();
    Lane findLaneByLaneIdAndActiveTrue(Long laneId);
    boolean existsLaneByLaneIdAndActiveTrue(Long id);
}
