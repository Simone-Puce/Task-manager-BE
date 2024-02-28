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
    public Lane getLaneByCode(String laneCode) {
        return validateLaneByCode(laneCode);
    }

    @Override
    public List<Lane> getAllLanes() {
        return laneRepository.findAll();
    }

    @Override
    public Lane createLane(Lane lane) {
        checkForDuplicateLane(lane.getLaneCode());
        laneRepository.save(lane);
        return lane;
    }

    @Override
    public Lane updateLaneByCode(String laneCode, Lane lane) {
        List<Lane> tasks = laneRepository.findAll();
        Lane taskExisting = validateLaneByCode(laneCode);

        List<Lane> laneExcludingSelectedLane = new ArrayList<>();

        for (Lane t : tasks) {
            if (!Objects.equals(t.getLaneCode(), laneCode)) {
                laneExcludingSelectedLane.add(t);
            }
        }
        taskExisting.setLaneCode(lane.getLaneCode());
        taskExisting.setLaneName(lane.getLaneName());


        if (laneExcludingSelectedLane.isEmpty()) {
            laneRepository.save(taskExisting);
        } else {
            for (Lane t : laneExcludingSelectedLane) {
                if (t.getLaneCode().equals(taskExisting.getLaneCode())) {
                    throw new DuplicateException("CODE: " + laneCode, "CODE: " + lane.getLaneCode());
                }
            }
            laneRepository.save(taskExisting);
        }

        return taskExisting;
    }

    @Override
    public void deleteLaneByCode(String laneCode) {
        Lane lane = validateLaneByCode(laneCode);
        laneRepository.deleteById(lane.getId());
    }

    public Lane validateLaneByCode(String code) {
        Lane existingCode = laneRepository.findLaneByLaneCode(code);

        if (Objects.isNull(existingCode)) {
            throw new ResourceNotFoundException("Error: Lane with CODE: " + code + " not found.");
        }
        return existingCode;
    }

    private void checkForDuplicateLane(String laneCode) {
        Lane laneByCode = laneRepository.findLaneByLaneCode(laneCode);
        if (!Objects.isNull(laneByCode)) {
            throw new DuplicateException("CODE: " + laneCode, "CODE: " + laneByCode.getLaneCode());
        }
    }
}
