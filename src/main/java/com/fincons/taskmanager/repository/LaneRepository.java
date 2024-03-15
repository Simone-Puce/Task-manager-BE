package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.Lane;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LaneRepository extends JpaRepository<Lane, Long> {
    @Query(value = "SELECT l " +
                    "FROM Lane l " +
                    "LEFT JOIN FETCH l.tasks t " +
                    "WHERE l.laneId = :laneId " +
                    "AND l.active = true " +
                    "AND (t.active = true OR t IS NULL) "
    )
    Lane findLaneIdForTasksAllTrue(@Param("laneId")Long laneId);
    @Query(value = "SELECT l " +
            "FROM Lane l " +
            "LEFT JOIN FETCH l.tasks t " +
            "WHERE l.active = true " +
            "AND (t.active = true OR t IS NULL) "
    )
    List<Lane> findAllForTasksAllTrue();
    List<Lane> findAllByActiveTrue();
    Lane findLaneByLaneIdAndActiveTrue(Long laneId);
    boolean existsLaneByLaneIdAndActiveTrue(Long id);
}
