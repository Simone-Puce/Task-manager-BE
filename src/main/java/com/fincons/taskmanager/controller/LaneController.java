package com.fincons.taskmanager.controller;


import com.fincons.taskmanager.dto.LaneDTO;
import com.fincons.taskmanager.entity.Lane;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.mapper.LaneMapper;
import com.fincons.taskmanager.service.laneService.LaneService;
import com.fincons.taskmanager.utility.GenericResponse;
import com.fincons.taskmanager.utility.ValidateFields;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/task-manager")
public class LaneController {

    @Autowired
    private LaneService laneService;
    @Autowired
    private LaneMapper modelMapperLane;

    @GetMapping(value = "${lane.find-by-code}")
    public ResponseEntity<GenericResponse<LaneDTO>> getLaneByCode(@RequestParam String code) {
        try {
            ValidateFields.validateSingleField(code);
            Lane lane = laneService.getLaneByCode(code);
            LaneDTO laneDTO = modelMapperLane.mapToDTO(lane);
            GenericResponse<LaneDTO> response = GenericResponse.success(
                    laneDTO,
                    "Success: Found Lane with CODE " + code + ".",
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.error(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        } catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rnfe.getMessage(),
                            HttpStatus.NOT_FOUND
                    )
            );
        }
    }
    @GetMapping(value = "${lane.list}")
    public ResponseEntity<GenericResponse<List<LaneDTO>>> getAllLanes() {
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
    public ResponseEntity<GenericResponse<LaneDTO>> createLane(@RequestBody LaneDTO laneDTO) {
        try {
            validateLaneFields(laneDTO);

            Lane laneMapped = modelMapperLane.mapToEntity(laneDTO);

            Lane lane = laneService.createLane(laneMapped);

            LaneDTO laneDTO2 = modelMapperLane.mapToDTO(lane);

            GenericResponse<LaneDTO> response = GenericResponse.success(
                    laneDTO2,
                    "Success: Lane with code: " + lane.getLaneCode() + " has been successfully updated!",
                    HttpStatus.OK);
            return ResponseEntity.ok(response);

        }
        catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.error(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        } catch (DuplicateException dne) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    GenericResponse.error(
                            dne.getMessage(),
                            HttpStatus.CONFLICT
                    )
            );
        }
    }
    @PutMapping(value = "${lane.put}")
    public ResponseEntity<GenericResponse<LaneDTO>> updateLaneByCode(@RequestParam String laneCode, @RequestBody LaneDTO laneDTO) {
        try {
            ValidateFields.validateSingleField(laneCode);

            validateLaneFields(laneDTO);

            Lane laneMapped = modelMapperLane.mapToEntity(laneDTO);

            Lane lane = laneService.updateLaneByCode(laneCode, laneMapped);

            LaneDTO laneDTO2 = modelMapperLane.mapToDTO(lane);

            GenericResponse<LaneDTO> response = GenericResponse.success(
                    laneDTO2,
                    "Success: Lane with code: " + laneCode + " has been successfully updated!",
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        } catch (ResourceNotFoundException rfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rfe.getMessage(),
                            HttpStatus.NOT_FOUND
                    )
            );
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.error(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        } catch (DuplicateException dne) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    GenericResponse.error(
                            dne.getMessage(),
                            HttpStatus.CONFLICT
                    )
            );
        }
    }
    @PutMapping(value = "${lane.delete}")
    public ResponseEntity<GenericResponse<LaneDTO>> deleteLaneByCode(@RequestParam String laneCode) {
        try {
            ValidateFields.validateSingleField(laneCode);
            laneService.deleteLaneByCode(laneCode);
            GenericResponse<LaneDTO> response = GenericResponse.empty(
                    "Success: Lane with code: " + laneCode + " has been successfully deleted! ",
                    HttpStatus.OK
            );

            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.error(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        } catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rnfe.getMessage(),
                            HttpStatus.NOT_FOUND));
        }
    }
    private void validateLaneFields(LaneDTO laneDTO) {
        if (Strings.isEmpty(laneDTO.getLaneCode()) ||
                Strings.isEmpty(laneDTO.getLaneName())) {
            throw new IllegalArgumentException("Error: The fields of the lane can't be null or empty.");
        }
    }
}

