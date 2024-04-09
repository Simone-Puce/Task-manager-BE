package com.fincons.taskmanager.controller;

import com.fincons.taskmanager.exception.DuplicateException;
import com.fincons.taskmanager.exception.ResourceNotFoundException;
import com.fincons.taskmanager.exception.RoleException;
import com.fincons.taskmanager.utility.GenericResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.io.IOException;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger log = LogManager.getLogger(ControllerExceptionHandler.class);
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<GenericResponse<ErrorMessage>> httpMessageNotReadableException(HttpMessageNotReadableException hmnre) {
        log.warn(hmnre.getMessage());
        GenericResponse<ErrorMessage> response = GenericResponse.error(
                hmnre.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GenericResponse<ErrorMessage>> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException matme) {
        log.warn(matme.getMessage());
        GenericResponse<ErrorMessage> response = GenericResponse.error(
                matme.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(IOException.class)
    public ResponseEntity<GenericResponse<?>> iOException(IOException ioe){
        log.warn(ioe.getMessage());
        GenericResponse<?> response = GenericResponse.error(
                ioe.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<GenericResponse<ErrorMessage>> missingServletRequestParameterException(MissingServletRequestParameterException msrpe){
        log.warn(msrpe.getMessage());
        GenericResponse<ErrorMessage> response = GenericResponse.error(
                msrpe.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return ResponseEntity.badRequest().body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<GenericResponse<?>> illegalArgumentException(IllegalArgumentException iae) {
        log.warn(iae.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                GenericResponse.error(
                        iae.getMessage(),
                        HttpStatus.BAD_REQUEST
                )
        );
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<GenericResponse<?>> resourceNotFoundException(ResourceNotFoundException rnfe){
        log.warn(rnfe.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                GenericResponse.error(
                        rnfe.getMessage(),
                        HttpStatus.NOT_FOUND
                )
        );
    }
    @ExceptionHandler(DuplicateException.class)
    public ResponseEntity<GenericResponse<?>> duplicateException(DuplicateException dne){
        log.warn(dne.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                GenericResponse.error(
                        dne.getMessage(),
                        HttpStatus.CONFLICT
                )
        );
    }
    @ExceptionHandler(RoleException.class)
    public ResponseEntity<GenericResponse<?>> roleException(RoleException re){
        log.warn(re.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                GenericResponse.error(
                        re.getMessage(),
                        HttpStatus.UNAUTHORIZED
                )
        );
    }
}
