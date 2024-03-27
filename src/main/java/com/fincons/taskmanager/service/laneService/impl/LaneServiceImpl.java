package com.fincons.taskmanager.service.laneService.impl;

import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.entity.Lane;
import com.fincons.taskmanager.entity.Task;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.repository.AttachmentRepository;
import com.fincons.taskmanager.repository.LaneRepository;
import com.fincons.taskmanager.service.boardService.impl.BoardServiceImpl;
import com.fincons.taskmanager.service.laneService.LaneService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class LaneServiceImpl implements LaneService {
    @Autowired
    private LaneRepository laneRepository;
    @Autowired
    private BoardServiceImpl boardServiceImpl;
    @Autowired
    private AttachmentRepository attachmentRepository;
    private static final Logger log = LogManager.getLogger(LaneServiceImpl.class);
    @Override
    public Lane getLaneById(Long laneId) {
        existingLaneById(laneId);
        return filterLaneForLanesTrue(laneRepository.findLaneByLaneIdAndActiveTrue(laneId));
    }
    @Override
    public List<Lane> getAllLanes() {
        return sortLanesList(filterLanesForTasksTrue(laneRepository.findAllByActiveTrue()));
    }
    @Override
    public Lane createLane(Lane lane) {
        Board board = boardServiceImpl.validateBoardById(lane.getBoard().getBoardId());
        lane.setBoard(board);
        lane.setActive(true);
        laneRepository.save(lane);
        log.info("New Lane saved in the repository with ID {}.", lane.getLaneId());
        return lane;
    }
    @Override
    public Lane updateLaneById(Long laneId, Lane lane) {
        existingLaneById(laneId);
        Lane laneExisting = laneRepository.findLaneByLaneIdAndActiveTrue(laneId);
        laneExisting.setLaneName(lane.getLaneName());
        laneRepository.save(laneExisting);
        log.info("Updated Lane in the repository with ID {}.", laneExisting.getLaneId());
        return filterLaneForLanesTrue(laneExisting);
    }
    @Override
    @Transactional
    public void deleteLaneById(Long laneId) {
        Lane lane = validateLaneById(laneId);
        lane.setActive(false);
        lane.getTasks().forEach(task -> {
            log.info("Task with ID {} deleted from the repository.", task.getTaskId());
            task.setActive(false);
            task.getAttachments().forEach(attachment -> {
                log.info("Attachment with ID {} deleted from the repository.", attachment.getAttachmentId());
                attachmentRepository.delete(attachment);
            });
        });
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
        log.info("Lane retrieved from repository with ID: {} exists", id);
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
    private List<Lane> sortLanesList(List<Lane>lanes){
        List<Lane> sortedLanes = new ArrayList<>(lanes);
        sortedLanes.sort(Comparator.comparing(Lane::getLaneName)
                .thenComparing(Lane::getLaneId));
        return sortedLanes;
    }
}
