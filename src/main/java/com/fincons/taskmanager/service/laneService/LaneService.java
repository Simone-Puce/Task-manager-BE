package com.fincons.taskmanager.service.laneService;

import com.fincons.taskmanager.entity.Lane;

import java.util.List;

public interface LaneService {

    Lane getLaneById(Long laneId);
    List<Lane> getAllLanes();
    Lane createLane(Lane lane);
    Lane updateLaneById(Long laneId, Lane lane);
    void deleteLaneById(Long laneId);
}
