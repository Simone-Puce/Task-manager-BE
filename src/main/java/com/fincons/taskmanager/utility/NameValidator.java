package com.fincons.taskmanager.utility;

import java.util.regex.Pattern;

public class NameValidator {
    private static final String NAME_REGEX_CONTAIN_LOWERCASE_AND_UPPERCASE_AND_NUMBER = "^[a-zA-Z0-9 ]*$";
    private static final String NAME_REGEX_WITH_SPACE_IN_BUT_NOT_BEFORE_AND_AFTER_WITHIN_DOUBLE_SPACE = "(\\w)(?>\\w|\\s(?!\\s))*(?<!\\s)$";


    public static String nameValidator(String inputText){
        boolean isMatch = Pattern.matches(NAME_REGEX_CONTAIN_LOWERCASE_AND_UPPERCASE_AND_NUMBER, inputText);
        if (isMatch){
            String textWithoutDoubleSpace = replaceDoubleOrMoreSpaces(inputText);
            String textWithoutSpaceBeforeAndAfter = replaceSpaceBeforeAndAfter(textWithoutDoubleSpace);
            boolean isMatch2 = Pattern.matches(NAME_REGEX_WITH_SPACE_IN_BUT_NOT_BEFORE_AND_AFTER_WITHIN_DOUBLE_SPACE, textWithoutSpaceBeforeAndAfter);
            if (isMatch2){
                return textWithoutSpaceBeforeAndAfter;
            }
            else {
                throw new IllegalArgumentException("Error: Conversion of the spaces was not successful.");
            }

        } else {
            throw new IllegalArgumentException("Error: Name must only contain lowercase letters, uppercase letters, or numbers, without any special characters.");
        }
    }
    private static String replaceDoubleOrMoreSpaces(String input) {
        return input.replaceAll("\\s+", " ");
    }
    private static String replaceSpaceBeforeAndAfter(String textWithoutDoubleSpace) {
        return textWithoutDoubleSpace.trim();
    }
}

