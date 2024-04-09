package com.fincons.taskmanager.controller;


import com.fincons.taskmanager.dto.LaneDTO;
import com.fincons.taskmanager.entity.Lane;
import com.fincons.taskmanager.exception.RoleException;
import com.fincons.taskmanager.mapper.LaneMapper;
import com.fincons.taskmanager.service.laneService.LaneService;
import com.fincons.taskmanager.utility.GenericResponse;
import com.fincons.taskmanager.utility.MaxCharLength;
import com.fincons.taskmanager.utility.SpaceAndFormatValidator;
import com.fincons.taskmanager.utility.ValidateFields;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/task-manager")
public class LaneController {

    @Autowired
    private LaneService laneService;
    @Autowired
    private LaneMapper modelMapperLane;
    private static final Logger log = LogManager.getLogger(LaneController.class);

    @GetMapping(value = "${lane.find-by-id}")
    public ResponseEntity<GenericResponse<LaneDTO>> getLaneById(@RequestParam Long id) {
        log.info("Received {} request for Lane with ID: {}", RequestMethod.GET, id);
        ValidateFields.validateSingleFieldLong(id);
        Lane lane = laneService.getLaneById(id);
        LaneDTO laneDTO = modelMapperLane.mapToDTO(lane);
        GenericResponse<LaneDTO> response = GenericResponse.success(
                laneDTO,
                "Success: Found Lane with ID " + id + ".",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }
    @GetMapping(value = "${lane.list}")
    public ResponseEntity<GenericResponse<List<LaneDTO>>> getAllLanes() {
        log.info("Received request to retrieve all Lane lists.");
        List<Lane> lanes = laneService.getAllLanes();
        List<LaneDTO> laneDTOs = modelMapperLane.mapEntitiesToDTOs(lanes);
        GenericResponse<List<LaneDTO>> response = GenericResponse.success(
                laneDTOs,
                "Success:" + (laneDTOs.isEmpty() || laneDTOs.size() == 1 ? " Found " : " Founds ") + laneDTOs.size() +
                        (laneDTOs.isEmpty() || laneDTOs.size() == 1 ? " lane" : " lanes") + ".",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping(value = "${lane.create}")
    public ResponseEntity<GenericResponse<LaneDTO>> createLane(@RequestBody LaneDTO laneDTO) throws RoleException {
        log.info("Received {} request for create new lane", RequestMethod.POST);
        validateLaneDTO(laneDTO);
        Lane laneMapped = modelMapperLane.mapToEntity(laneDTO);
        Lane lane = laneService.createLane(laneMapped);
        LaneDTO laneDTO2 = modelMapperLane.mapToDTO(lane);
        GenericResponse<LaneDTO> response = GenericResponse.success(
                laneDTO2,
                "Success: Lane with id: " + lane.getLaneId() + " has been successfully updated!",
                HttpStatus.OK);
        return ResponseEntity.ok(response);

    }
    @PutMapping(value = "${lane.put}")
    public ResponseEntity<GenericResponse<LaneDTO>> updateLaneById(@RequestParam Long laneId, @RequestBody LaneDTO laneDTO) throws RoleException {
        log.info("Received {} request to modify Lane with ID {}", RequestMethod.PUT, laneId);
        ValidateFields.validateSingleFieldLong(laneId);
        validateLaneDTO(laneDTO);
        Lane laneMapped = modelMapperLane.mapToEntity(laneDTO);
        Lane lane = laneService.updateLaneById(laneId, laneMapped);
        LaneDTO laneDTO2 = modelMapperLane.mapToDTO(lane);
        GenericResponse<LaneDTO> response = GenericResponse.success(
                laneDTO2,
                "Success: Lane with id: " + laneId + " has been successfully updated!",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }
    @PutMapping(value = "${lane.delete}")
    public ResponseEntity<GenericResponse<LaneDTO>> deleteLaneById(@RequestParam Long laneId) throws RoleException {
        log.info("Received {} request for delete Task with ID: {}", RequestMethod.DELETE, laneId);
        ValidateFields.validateSingleFieldLong(laneId);
        laneService.deleteLaneById(laneId);
        GenericResponse<LaneDTO> response = GenericResponse.empty(
                "Success: Lane with id: " + laneId + " has been successfully deleted! ",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }
    private void validateLaneDTO(LaneDTO LaneDTO) {
        log.debug("Start validating LaneDTO");
        validateLaneFields(LaneDTO);
        String newLaneName = SpaceAndFormatValidator.spaceAndFormatValidator(LaneDTO.getLaneName());
        MaxCharLength.validateNameLength(newLaneName);
        LaneDTO.setLaneName(newLaneName);
        log.debug("LaneDTO validation completed successfully");
    }
    private void validateLaneFields(LaneDTO laneDTO) {
        log.debug("Start validation fields laneDTO");
        if (Strings.isEmpty(laneDTO.getLaneName())) {
            throw new IllegalArgumentException("Error: The fields of the lane can't be null or empty.");
        }
        log.debug("LaneDTO validation fields was successful");
    }
}

