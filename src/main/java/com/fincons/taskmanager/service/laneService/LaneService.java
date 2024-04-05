package com.fincons.taskmanager.service.laneService;

import com.fincons.taskmanager.entity.Lane;
import com.fincons.taskmanager.exception.RoleException;

import java.util.List;

public interface LaneService {

    Lane getLaneById(Long laneId);
    List<Lane> getAllLanes();
    Lane createLane(Lane lane) throws RoleException;
    Lane updateLaneById(Long laneId, Lane lane) throws RoleException;
    void deleteLaneById(Long laneId) throws RoleException;
}
