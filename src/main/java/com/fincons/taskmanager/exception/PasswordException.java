package com.fincons.taskmanager.exception;

public class PasswordException extends Exception{

    public PasswordException(String message) {
        super(message);
    }

    public static String  passwordDoesNotRespectRegexException() {
        return "Password does not respect regex !";
    }

    public static String  invalidPasswordException() {
        return "Invalid Password!";
    }

}
