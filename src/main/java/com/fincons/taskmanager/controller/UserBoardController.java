package com.fincons.taskmanager.controller;

import com.fincons.taskmanager.dto.UserBoardDTO;
import com.fincons.taskmanager.entity.UserBoard;
import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.mapper.UserBoardMapper;
import com.fincons.taskmanager.service.userBoardService.UserBoardService;
import com.fincons.taskmanager.utility.GenericResponse;
import com.fincons.taskmanager.utility.ValidateFields;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin("*")
@RequestMapping("/task-manager")
public class UserBoardController {
    @Autowired
    private UserBoardService userBoardService;
    @Autowired
    private UserBoardMapper modelMapperUserBoard;

    @GetMapping(value = "${user.board.find.boards.by.user}")
    public ResponseEntity<GenericResponse<List<UserBoardDTO>>> findBoardsByUser(@RequestParam String email){
        try{
            ValidateFields.validateSingleField(email);
            List<UserBoard> usersBoards = userBoardService.findBoardsByUser(email);
            List<UserBoardDTO> userBoardDTOs = modelMapperUserBoard.mapEntitiesToDTOs(usersBoards);

            GenericResponse<List<UserBoardDTO>> response = GenericResponse.success(
                    userBoardDTOs,
                    "Success: There are all BOARDS for the User with email : " + email,
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

    @PostMapping(value = "${user.board.create}")
    public ResponseEntity<GenericResponse<UserBoardDTO>> createUserBoard(@RequestBody UserBoardDTO userBoardDTO){
        try{
            validateUserBoardFields(userBoardDTO);
            UserBoard userBoardMapped = modelMapperUserBoard.mapToEntity(userBoardDTO);
            UserBoard userBoard = userBoardService.createUserBoard(userBoardMapped);

            UserBoardDTO userBoardDTO2 = modelMapperUserBoard.mapToDTO(userBoard);
            GenericResponse<UserBoardDTO> response = GenericResponse.success(
                    userBoardDTO2,
                    "Success: Addition of relationship between USER with email: " + userBoardDTO2.getEmail() + " and BOARD with CODE: " + userBoardDTO2.getBoardCode(),
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
    @PutMapping(value = "${user.board.put}")
    public ResponseEntity<GenericResponse<UserBoardDTO>> updateUserBoard(@RequestParam String email,
                                                                         @RequestParam String boardCode,
                                                                         @RequestBody UserBoardDTO userBoardDTO) {
        try {
            ValidateFields.validateSingleField(email);
            ValidateFields.validateSingleField(boardCode);
            validateUserBoardFields(userBoardDTO);
            UserBoard userBoardMapped = modelMapperUserBoard.mapToEntity(userBoardDTO);
            UserBoard userBoard = userBoardService.updateUserBoard(email, boardCode, userBoardMapped);
            UserBoardDTO userBoardDTO2 = modelMapperUserBoard.mapToDTO(userBoard);
            GenericResponse<UserBoardDTO> response = GenericResponse.success(
                    userBoardDTO2,
                    "Success: Addition of relationship between User with EMAIL: " + email + " and Board with CODE: " + boardCode,
                    HttpStatus.OK
            );
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException iae) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                    GenericResponse.empty(
                            iae.getMessage(),
                            HttpStatus.BAD_REQUEST
                    )
            );
        } catch (ResourceNotFoundException rnfe) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                    GenericResponse.error(
                            rnfe.getMessage(),
                            HttpStatus.NOT_FOUND));
        } catch (DuplicateException dne) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    GenericResponse.error(
                            dne.getMessage(),
                            HttpStatus.CONFLICT
                    )
            );
        }
    }
    @DeleteMapping(value = "${user.board.delete}")
    public ResponseEntity<GenericResponse<UserBoardDTO>> deleteUserBoard(@RequestParam String email,
                                                                         @RequestParam String boardCode){
        try{
            ValidateFields.validateSingleField(email);
            ValidateFields.validateSingleField(boardCode);
            UserBoard userBoard = userBoardService.deleteUserBoard(email, boardCode);
            UserBoardDTO userBoardDTO = modelMapperUserBoard.mapToDTO(userBoard);
            GenericResponse<UserBoardDTO> response = GenericResponse.success(
                    userBoardDTO,
                    "Success: Delete relationship between USER with EMAIL: " + email + " and Board with CODE: " + boardCode,
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
    private void validateUserBoardFields(UserBoardDTO userBoardDTO) {
        if (Strings.isEmpty(userBoardDTO.getEmail()) ||
                Strings.isEmpty(userBoardDTO.getBoardCode()) ||
                Strings.isEmpty(userBoardDTO.getRoleCode()))  {
            throw new IllegalArgumentException("Error: The fields of the user-board can't be null or empty.");
        }
    }
}
