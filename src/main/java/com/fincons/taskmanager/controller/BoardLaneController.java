package com.fincons.taskmanager.controller;

import com.fincons.taskmanager.dto.BoardLaneDTO;
import com.fincons.taskmanager.entity.BoardLane;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.mapper.BoardLaneMapper;
import com.fincons.taskmanager.service.boardLaneService.BoardLaneService;
import com.fincons.taskmanager.utility.GenericResponse;
import com.fincons.taskmanager.utility.ValidateFields;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/task-manager")
public class BoardLaneController {

    @Autowired
    private BoardLaneService boardLaneService;
    @Autowired
    private BoardLaneMapper modelMapperBoardLane;

    @PostMapping(value = "${board.lane.create}")
    public ResponseEntity<GenericResponse<BoardLaneDTO>> createBoardLane(@RequestBody BoardLaneDTO boardLaneDTO) {
        try {
            validateBoardLaneFields(boardLaneDTO);
            BoardLane boardLaneMapped = modelMapperBoardLane.mapToEntity(boardLaneDTO);
            BoardLane boardLane = boardLaneService.createBoardLane(boardLaneMapped);

            BoardLaneDTO boardLaneDTO2 = modelMapperBoardLane.mapToDTO(boardLane);
            GenericResponse<BoardLaneDTO> response = GenericResponse.success(
                    boardLaneDTO2,
                    "Success: Addition of relationship between board with ID: " + boardLaneDTO2.getBoardId() + " and lane with ID: " + boardLaneDTO2.getLaneId(),
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        }
        catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.empty(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        }
        catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rnfe.getMessage(),
                            HttpStatus.NOT_FOUND));
        }
        catch (DuplicateException dne) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    GenericResponse.error(
                            dne.getMessage(),
                            HttpStatus.CONFLICT
                    )
            );
        }
    }
    @PutMapping(value = "${board.lane.put}")
    public ResponseEntity<GenericResponse<BoardLaneDTO>> updateBoardLane(@RequestParam Long boardId,
                                                                         @RequestParam Long laneId,
                                                                         @RequestBody BoardLaneDTO boardLaneDTO){
        try {
            ValidateFields.validateSingleFieldLong(boardId);
            ValidateFields.validateSingleFieldLong(laneId);
            validateBoardLaneFields(boardLaneDTO);
            BoardLane boardLaneMapped = modelMapperBoardLane.mapToEntity(boardLaneDTO);
            BoardLane boardLane = boardLaneService.updateBoardLane(boardId, laneId, boardLaneMapped);
            BoardLaneDTO boardLaneDTO2 = modelMapperBoardLane.mapToDTO(boardLane);
            GenericResponse<BoardLaneDTO> response = GenericResponse.success(
                    boardLaneDTO2,
                    "Success: Addition of relationship between board with ID: " + boardId + " and lane with ID: " + laneId,
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        }
        catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.empty(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        }
        catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rnfe.getMessage(),
                            HttpStatus.NOT_FOUND));
        }
        catch (DuplicateException dne) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    GenericResponse.error(
                            dne.getMessage(),
                            HttpStatus.CONFLICT
                    )
            );
        }
    }
    @DeleteMapping(value = "${board.lane.delete}")
    public ResponseEntity<GenericResponse<BoardLaneDTO>> deleteBoardLane(@RequestParam Long boardId, @RequestParam Long laneId) {
        try {
            ValidateFields.validateSingleFieldLong(boardId);
            ValidateFields.validateSingleFieldLong(laneId);
            boardLaneService.deleteBoardLane(boardId, laneId);
            GenericResponse<BoardLaneDTO> response = GenericResponse.empty(
                    "Success: Delete relationship between board with ID: " + boardId + " and lane with ID: " + laneId,
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        }
        catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.empty(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        }
        catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rnfe.getMessage(),
                            HttpStatus.NOT_FOUND));
        }
        catch (DuplicateException dne) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    GenericResponse.error(
                            dne.getMessage(),
                            HttpStatus.CONFLICT
                    )
            );
        }
    }
    private void validateBoardLaneFields(BoardLaneDTO boardLaneDTO) {
        if (ValidateFields.isValidTaskId(boardLaneDTO.getBoardId()) ||
                ValidateFields.isValidTaskId(boardLaneDTO.getLaneId())) {
            throw new IllegalArgumentException("Error: The fields of the boardLane can't be null or empty.");
        }
    }
}
