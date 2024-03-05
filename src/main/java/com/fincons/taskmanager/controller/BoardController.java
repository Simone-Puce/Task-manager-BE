package com.fincons.taskmanager.controller;


import com.fincons.taskmanager.dto.BoardDTO;
import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.mapper.BoardMapper;
import com.fincons.taskmanager.service.boardService.BoardService;
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
public class BoardController {
    
    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardMapper modelMapperBoard;

    @GetMapping(value = "${board.find-by-code}")
    public ResponseEntity<GenericResponse<BoardDTO>> getBoardByCode(@RequestParam String code) {
        try {
            ValidateFields.validateSingleField(code);
            Board board = boardService.getBoardByCode(code);
            BoardDTO boardDTO = modelMapperBoard.mapToDTO(board);
            GenericResponse<BoardDTO> response = GenericResponse.success(
                    boardDTO,
                    "Success: Found Board with CODE " + code + ".",
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
    @GetMapping(value = "${board.list}")
    public ResponseEntity<GenericResponse<List<BoardDTO>>> getAllBoards() {
        List<Board> boards = boardService.getAllBoards();
        List<BoardDTO> boardDTOs = modelMapperBoard.mapEntitiesToDTOs(boards);
        GenericResponse<List<BoardDTO>> response = GenericResponse.success(
                boardDTOs,
                "Success:" + (boardDTOs.isEmpty() || boardDTOs.size() == 1 ? " Found " : " Founds ") + boardDTOs.size() +
                        (boardDTOs.isEmpty() || boardDTOs.size() == 1 ? " board" : " boards") + ".",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }
    @PostMapping(value = "${board.create}")
    public ResponseEntity<GenericResponse<BoardDTO>> createBoard(@RequestBody BoardDTO boardDTO) {
        try {
            validateBoardFields(boardDTO);

            Board boardMapped = modelMapperBoard.mapToEntity(boardDTO);

            Board board = boardService.createBoard(boardMapped);

            BoardDTO boardDTO2 = modelMapperBoard.mapToDTO(board);

            GenericResponse<BoardDTO> response = GenericResponse.success(
                    boardDTO2,
                    "Success: Board with code: " + board.getBoardCode() + " has been successfully updated!",
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
    @PutMapping(value = "${board.put}")
    public ResponseEntity<GenericResponse<BoardDTO>> updateBoardByCode(@RequestParam String boardCode, @RequestBody BoardDTO boardDTO) {
        try {
            ValidateFields.validateSingleField(boardCode);

            validateBoardFields(boardDTO);

            Board boardMapped = modelMapperBoard.mapToEntity(boardDTO);

            Board board = boardService.updateBoardByCode(boardCode, boardMapped);

            BoardDTO boardDTO2 = modelMapperBoard.mapToDTO(board);

            GenericResponse<BoardDTO> response = GenericResponse.success(
                    boardDTO2,
                    "Success: Board with code: " + boardCode + " has been successfully updated!",
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
    @PutMapping(value = "${board.delete}")
    public ResponseEntity<GenericResponse<BoardDTO>> deleteBoardByCode(@RequestParam String boardCode) {
        try {
            ValidateFields.validateSingleField(boardCode);
            boardService.deleteBoardByCode(boardCode);
            GenericResponse<BoardDTO> response = GenericResponse.empty(
                    "Success: Board with code: " + boardCode + " has been successfully deleted! ",
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
    private void validateBoardFields(BoardDTO boardDTO) {
        if (Strings.isEmpty(boardDTO.getBoardCode()) ||
                Strings.isEmpty(boardDTO.getBoardName())) {
            throw new IllegalArgumentException("Error: The fields of the board can't be null or empty.");
        }
    }
}
