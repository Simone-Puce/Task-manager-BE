package com.fincons.taskmanager.utility;

import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
public class ValidateFields {

    private Long field;

    public static void validateSingleField(String field){
        if (field == null){
            throw new IllegalArgumentException("The field can't be null");
        }
        if (field.isEmpty()){
            throw new IllegalArgumentException("The field can't be empty");
        }
    }
    public static void validateSingleFieldLong(Long field) {
        if (field == null) {
            throw new IllegalArgumentException("The field id can't be null");
        }
        if (field <= 0) {
            throw new IllegalArgumentException("The attachment ID must be positive or greater than zero");
        }
    }
    public static boolean isValidTaskId(Long taskId) {
        if (taskId == null || taskId <= 0) {
            return true;
        } else {
            return false;
        }
    }
}
