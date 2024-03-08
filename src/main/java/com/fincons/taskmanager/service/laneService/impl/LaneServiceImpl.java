package com.fincons.taskmanager.service.laneService.impl;

import com.fincons.taskmanager.entity.Lane;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.LaneRepository;
import com.fincons.taskmanager.service.laneService.LaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class LaneServiceImpl implements LaneService {

    @Autowired
    private LaneRepository laneRepository;

    @Override
    public Lane getLaneById(Long laneId) {
        return validateLaneById(laneId);
    }

    @Override
    public List<Lane> getAllLanes() {
        return laneRepository.findAllByActiveTrue();
    }

    @Override
    public Lane createLane(Lane lane) {
        lane.setActive(true);
        laneRepository.save(lane);
        return lane;
    }

    @Override
    public Lane updateLaneById(Long laneId, Lane lane) {
        Lane taskExisting = validateLaneById(laneId);
        taskExisting.setLaneName(lane.getLaneName());
        laneRepository.save(taskExisting);
        return taskExisting;
    }

    @Override
    public void deleteLaneById(Long laneId) {
        Lane lane = validateLaneById(laneId);
        lane.setActive(false);
        laneRepository.save(lane);
    }

    public Lane validateLaneById(Long id) {
        Lane existingId = laneRepository.findLaneByLaneIdAndActiveTrue(id);
        if (Objects.isNull(existingId)) {
            throw new ResourceNotFoundException("Error: Lane with ID: " + id + " not found.");
        }
        return existingId;
    }
}
