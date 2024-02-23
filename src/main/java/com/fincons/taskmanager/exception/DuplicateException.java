package com.fincons.taskmanager.exception;

public class DuplicateException extends RuntimeException{
    public DuplicateException(String newObject, String existingObject) {
        super("Error: Failed to insert " + newObject + " into the database, the " + existingObject + " already exist");
    }
}