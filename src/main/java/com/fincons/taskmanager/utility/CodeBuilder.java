package com.fincons.taskmanager.utility;


import java.util.regex.Pattern;

public class CodeBuilder {

    private static final String CODE_REGEX_FORMAT = "^([A-Z0-9]+)-([0-9]+)$";

    public static String transformToCode(String input, Long id) {
        String textUpperCase = convertToUpperCase(input);
        String noSpace = replaceSingleSpaces(textUpperCase);
        String textToCode = (noSpace + "-" + id);
        validateText(textToCode);
        return textToCode;
    }
    private static String convertToUpperCase(String input) {
        return input.toUpperCase();
    }
    private static void validateText(String textUpperCase){
        boolean isMatch = Pattern.matches(CODE_REGEX_FORMAT, textUpperCase);
        if (!isMatch){
            throw new IllegalArgumentException("Error: Code validation failed");
        }
    }
    private static String replaceSingleSpaces(String input) {
        return input.replaceAll(" ", "");
    }
}
