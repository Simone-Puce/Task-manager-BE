package com.fincons.taskmanager.service.laneService.impl;

import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.Lane;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.LaneRepository;
import com.fincons.taskmanager.service.boardService.impl.BoardServiceImpl;
import com.fincons.taskmanager.service.laneService.LaneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class LaneServiceImpl implements LaneService {
    @Autowired
    private LaneRepository laneRepository;
    @Autowired
    private BoardServiceImpl boardServiceImpl;

    @Override
    public Lane getLaneById(Long laneId) {
        existingLaneById(laneId);
        Lane laneForTasks = laneRepository.findLaneIdForTasksAllTrue(laneId);
        if (Objects.isNull(laneForTasks)){
            Lane laneForTasksNonNull = laneRepository.findLaneByLaneIdAndActiveTrue(laneId);
            return filterLaneForLanesTrue(laneForTasksNonNull);
        }
        return laneForTasks;
    }
    @Override
    public List<Lane> getAllLanes() {
        List<Lane> lanes = laneRepository.findAllForTasksAllTrue();
        if (Objects.isNull(lanes)){
            List<Lane> lanesForTasks = laneRepository.findAllByActiveTrue();
            return filterLanesForTasksTrue(lanesForTasks);
        }
        return lanes;
    }
    @Override
    public Lane createLane(Lane lane) {
        Board board = boardServiceImpl.validateBoardById(lane.getBoard().getBoardId());
        lane.setBoard(board);
        lane.setActive(true);
        laneRepository.save(lane);
        return lane;
    }
    @Override
    public Lane updateLaneById(Long laneId, Lane lane) {
        existingLaneById(laneId);
        Lane laneExisting = laneRepository.findLaneIdForTasksAllTrue(laneId);
        laneExisting.setLaneName(lane.getLaneName());
        Board board = boardServiceImpl.validateBoardById(lane.getBoard().getBoardId());
        laneExisting.setBoard(board);
        laneRepository.save(laneExisting);
        return laneExisting;
    }
    @Override
    @Transactional
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
    public void existingLaneById(Long id){
        boolean existingId = laneRepository.existsLaneByLaneIdAndActiveTrue(id);
        if (!existingId){
            throw new ResourceNotFoundException("Error: Lane with ID: " + id + " not found.");
        }
    }
    private Lane filterLaneForLanesTrue(Lane lane) {
        List <Task> tasks = lane.getTasks().stream()
                .filter(Task::isActive)
                .toList();
        lane.setTasks(tasks);
        return lane;
    }
    private List<Lane> filterLanesForTasksTrue(List<Lane> lanes){
        return lanes.stream()
                .map(this::filterLaneForLanesTrue)
                .toList();
    }
}
