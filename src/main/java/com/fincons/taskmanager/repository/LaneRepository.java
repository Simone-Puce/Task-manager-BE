package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.Lane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LaneRepository extends JpaRepository<Lane, Long> {

    Lane findLaneByLaneIdAndActiveTrue(Long laneId);

    List<Lane> findAllByActiveTrue();

    List<Lane> findAllByActiveTrue();
}
