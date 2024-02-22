package com.fincons.taskmanager.exception;

import java.util.List;

public class RoleException extends Exception{


    String message;
    List<Long> list;

    public RoleException(String message) {
        super(message);
    }

    public RoleException(String message, List<Long> list){
        super(message);
        this.message = message;
        this.list = list;
    }

    public static String userHasNotPermission(){
        return "User does not have permission to modify the role!.";
    }

    public static String roleDoesNotRespectRegex(){
        return "Role does not respect regex of ROLE_***** .";
    }
    public static String roleExistException(){
        return "Role exist yet!";
    }





}
