package com.fincons.taskmanager.service.laneService;

import com.fincons.taskmanager.entity.Lane;

import java.util.List;

public interface LaneService {

    Lane getLaneByCode(String laneCode);
    List<Lane> getAllLanes();
    Lane createLane(Lane lane);
    Lane updateLaneByCode(String laneCode, Lane lane);
    void deleteLaneByCode(String laneCode);
}
