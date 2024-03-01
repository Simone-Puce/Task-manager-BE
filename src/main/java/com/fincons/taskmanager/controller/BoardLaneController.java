package com.fincons.taskmanager.controller;

import com.fincons.taskmanager.dto.BoardLaneDTO;
import com.fincons.taskmanager.dto.BoardDTO;
import com.fincons.taskmanager.dto.BoardLaneDTO;
import com.fincons.taskmanager.entity.BoardLane;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.mapper.BoardLaneMapper;
import com.fincons.taskmanager.mapper.BoardMapper;
import com.fincons.taskmanager.mapper.LaneMapper;
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
                    "Success: Addition of relationship between board with CODE: " + boardLaneDTO2.getBoardCode() + " and lane with CODE: " + boardLaneDTO2.getLaneCode(),
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
    public ResponseEntity<GenericResponse<BoardLaneDTO>> updateBoardLane(@RequestParam String boardCode,
                                                                         @RequestParam String laneCode,
                                                                         @RequestBody BoardLaneDTO boardLaneDTO){
        try {
            ValidateFields.validateSingleField(boardCode);
            ValidateFields.validateSingleField(laneCode);
            validateBoardLaneFields(boardLaneDTO);
            BoardLane boardLaneMapped = modelMapperBoardLane.mapToEntity(boardLaneDTO);
            BoardLane boardLane = boardLaneService.updateBoardLane(boardCode, laneCode, boardLaneMapped);
            BoardLaneDTO boardLaneDTO2 = modelMapperBoardLane.mapToDTO(boardLane);
            GenericResponse<BoardLaneDTO> response = GenericResponse.success(
                    boardLaneDTO2,
                    "Success: Addition of relationship between board with CODE: " + boardCode + " and lane with CODE: " + laneCode,
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
    public ResponseEntity<GenericResponse<BoardLaneDTO>> deleteBoardLane(@RequestParam String boardCode, @RequestParam String laneCode) {
        try {
            ValidateFields.validateSingleField(boardCode);
            ValidateFields.validateSingleField(laneCode);
            BoardLane boardLane = boardLaneService.deleteBoardLane(boardCode, laneCode);
            BoardLaneDTO boardLaneDTO = modelMapperBoardLane.mapToDTO(boardLane);
            GenericResponse<BoardLaneDTO> response = GenericResponse.success(
                    boardLaneDTO,
                    "Success: Delete relationship between board with CODE: " + boardCode + " and lane with CODE: " + laneCode,
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
        if (Strings.isEmpty(boardLaneDTO.getBoardCode()) ||
                Strings.isEmpty(boardLaneDTO.getLaneCode())) {
            throw new IllegalArgumentException("Error: The fields of the boardLane can't be null or empty.");
        }
    }
}
