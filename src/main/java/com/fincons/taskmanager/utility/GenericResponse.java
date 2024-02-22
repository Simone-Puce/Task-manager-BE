package com.fincons.taskmanager.utility;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse<T> {

    private HttpStatus status;
    private boolean success;
    private String message;
    private T data;

    public static <T> GenericResponse<T> empty(String message, HttpStatus status) {
        return success(null, message, status);
    }

    public static <T> GenericResponse<T> success(T data, String message, HttpStatus status) {
        return GenericResponse.<T>builder()
                .message(message)
                .data(data)
                .status(status)
                .success(true)
                .build();
    }

    public static <T> GenericResponse<T> error(String message, HttpStatus status) {
        return GenericResponse.<T>builder()
                .message(message)
                .status(status)
                .success(false)
                .build();
    }

    public GenericResponse(HttpStatus status, boolean success, String message) {
        this.status = status;
        this.success = success;
        this.message = message;
    }

    public GenericResponse(HttpStatus status, boolean success, T data) {
        this.status = status;
        this.success = success;
        this.data = data;
    }

}
