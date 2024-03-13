package com.fincons.taskmanager.service.laneService.impl;

import com.fincons.taskmanager.entity.Lane;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.BoardLaneRepository;
import com.fincons.taskmanager.repository.LaneRepository;
import com.fincons.taskmanager.service.laneService.LaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class LaneServiceImpl implements LaneService {

    @Autowired
    private LaneRepository laneRepository;
    @Autowired
    private BoardLaneRepository boardLaneRepository;

    @Override
    public Lane getLaneById(Long laneId) {
        existingLaneById(laneId);
        return laneRepository.findLaneIdForBoardsAllTrue(laneId);
    }

    @Override
    public List<Lane> getAllLanes() {
        return laneRepository.findAllForBoardsAllTrue();
    }

    @Override
    public Lane createLane(Lane lane) {
        lane.setActive(true);
        laneRepository.save(lane);
        return lane;
    }

    @Override
    public Lane updateLaneById(Long laneId, Lane lane) {
        existingLaneById(laneId);
        Lane taskExisting = laneRepository.findLaneIdForBoardsAllTrue(laneId);
        taskExisting.setLaneName(lane.getLaneName());
        laneRepository.save(taskExisting);
        return taskExisting;
    }

    @Override
    @Transactional
    public void deleteLaneById(Long laneId) {
        Lane lane = validateLaneById(laneId);
        lane.setActive(false);
        laneRepository.save(lane);
        boardLaneRepository.deleteByLane(lane);
    }

    public Lane validateLaneById(Long id) {
        Lane existingId = laneRepository.findLaneByLaneIdAndActiveTrue(id);
        if (Objects.isNull(existingId)) {
            throw new ResourceNotFoundException("Error: Lane with ID: " + id + " not found.");
        }
        return existingId;
    }
    public void existingLaneById(Long id){
        boolean existingId = laneRepository.existsLaneByLaneIdAndActiveTrue(id);
        if (!existingId){
            throw new ResourceNotFoundException("Error: Lane with ID: " + id + " not found.");
        }
    }
}
