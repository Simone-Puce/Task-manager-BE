package com.fincons.taskmanager.controller;

import com.fincons.taskmanager.utility.GenericResponse;
import org.modelmapper.spi.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class ControllerExceptionHandler {
    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ResponseEntity<GenericResponse<ErrorMessage>> httpMessageNotReadableException(HttpMessageNotReadableException hmnre) {
        GenericResponse<ErrorMessage> response = GenericResponse.error(
                hmnre.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return ResponseEntity.ok(response);
    }
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<GenericResponse<ErrorMessage>> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException matme) {
        GenericResponse<ErrorMessage> response = GenericResponse.error(
                matme.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return ResponseEntity.ok(response);
    }
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<GenericResponse<ErrorMessage>> missingServletRequestParameterException(MissingServletRequestParameterException msrpe){
        GenericResponse<ErrorMessage> response = GenericResponse.error(
                msrpe.getMessage(),
                HttpStatus.BAD_REQUEST
        );
        return ResponseEntity.ok(response);
    }
}
