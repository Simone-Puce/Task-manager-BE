package com.fincons.taskmanager.exception;

public class EmailException extends Exception{

    public EmailException() {
    }

    public EmailException(String message) {
        super(message);
    }

    public static  String emailInvalidOrExist(){
        return "Invalid or existing email";
    }




}
