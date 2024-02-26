package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.Lane;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LaneRepository extends JpaRepository<Lane, Long> {

    Lane findLaneByLaneCode(String code);
}
