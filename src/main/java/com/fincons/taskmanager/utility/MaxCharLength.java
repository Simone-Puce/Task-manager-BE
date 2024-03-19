package com.fincons.taskmanager.utility;

public class MaxCharLength {

    static int nameLengthMax = 255;
    static int descriptionLengthMax = 5000;
    public static void validateNameLength(String input){
        int lengthName = input.length();
        if(lengthName > nameLengthMax){
            throw new IllegalArgumentException("The text cannot exceed " + nameLengthMax +" characters");
        }
    }
    public static void validateDescriptionLength(String input){
        int lengthName = input.length();
        if(lengthName > descriptionLengthMax){
            throw new IllegalArgumentException("The text cannot exceed " + descriptionLengthMax +" characters");
        }
    }
}
