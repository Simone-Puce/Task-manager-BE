package com.fincons.taskmanager.controller;


import com.fincons.taskmanager.dto.BoardDTO;
import com.fincons.taskmanager.dto.TaskDTO;
import com.fincons.taskmanager.entity.Board;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.mapper.BoardMapper;
import com.fincons.taskmanager.service.boardService.BoardService;
import com.fincons.taskmanager.utility.GenericResponse;
import com.fincons.taskmanager.utility.MaxCharLength;
import com.fincons.taskmanager.utility.SpaceAndFormatValidator;
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

    @GetMapping(value = "${board.find-by-id}")
    public ResponseEntity<GenericResponse<BoardDTO>> getBoardById(@RequestParam Long id) {
        ValidateFields.validateSingleFieldLong(id);
        Board board = boardService.getBoardById(id);
        BoardDTO boardDTO = modelMapperBoard.mapToDTO(board);
        GenericResponse<BoardDTO> response = GenericResponse.success(
                boardDTO,
                "Success: Found Board with ID " + id + ".",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
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
        validateBoardDTO(boardDTO);
        Board boardMapped = modelMapperBoard.mapToEntity(boardDTO);
        Board board = boardService.createBoard(boardMapped);
        BoardDTO boardDTO2 = modelMapperBoard.mapToDTO(board);
        GenericResponse<BoardDTO> response = GenericResponse.success(
                boardDTO2,
                "Success: Board with id: " + board.getBoardId() + " has been successfully updated!",
                HttpStatus.OK);
        return ResponseEntity.ok(response);
    }
    @PutMapping(value = "${board.put}")
    public ResponseEntity<GenericResponse<BoardDTO>> updateBoardById(@RequestParam Long boardId, @RequestBody BoardDTO boardDTO) {
        ValidateFields.validateSingleFieldLong(boardId);
        validateBoardDTO(boardDTO);
        Board boardMapped = modelMapperBoard.mapToEntity(boardDTO);
        Board board = boardService.updateBoardById(boardId, boardMapped);
        BoardDTO boardDTO2 = modelMapperBoard.mapToDTO(board);
        GenericResponse<BoardDTO> response = GenericResponse.success(
                boardDTO2,
                "Success: Board with id: " + boardId + " has been successfully updated!",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }

    @PutMapping(value = "${board.delete}")
    public ResponseEntity<GenericResponse<BoardDTO>> deleteBoardById(@RequestParam Long boardId) {
        ValidateFields.validateSingleFieldLong(boardId);
        boardService.deleteBoardById(boardId);
        GenericResponse<BoardDTO> response = GenericResponse.empty(
                "Success: Board with id: " + boardId + " has been successfully deleted! ",
                HttpStatus.OK
        );
        return ResponseEntity.ok(response);
    }
    private void validateBoardDTO(BoardDTO boardDTO) {
        validateBoardFields(boardDTO);
        String newBoardName = SpaceAndFormatValidator.spaceAndFormatValidator(boardDTO.getBoardName());
        MaxCharLength.validateNameLength(newBoardName);
        boardDTO.setBoardName(newBoardName);
    }
    private void validateBoardFields(BoardDTO boardDTO) {
        if (Strings.isEmpty(boardDTO.getBoardName())) {
            throw new IllegalArgumentException("Error: The field of the board can't be null or empty.");
        }
    }
}
