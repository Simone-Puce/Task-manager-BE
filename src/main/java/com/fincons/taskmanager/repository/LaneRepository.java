package com.fincons.taskmanager.repository;

import com.fincons.taskmanager.entity.Lane;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LaneRepository extends JpaRepository<Lane, Long> {

    Lane findLaneByLaneCode(String code);

    List<Lane> findAllByActiveTrue();
}
