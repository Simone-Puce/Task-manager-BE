package com.fincons.taskmanager.genericsTest;

import org.junit.jupiter.api.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexTest {
    private static final String NAME_REGEX_CONTAIN_LOWERCASE_AND_UPPERCASE_AND_NUMBER = "^[a-zA-Z0-9 ]*$";

    private static final String NAME_REGEX_WITH_SPACE_IN_BUT_NOT_BEFORE_AND_AFTER_WITHIN_DOUBLE_SPACE = "(\\w)(?>\\w|\\s(?!\\s))*(?<!\\s)$";


    private static String replaceDoubleSpaces(String input) {
        Pattern pattern = Pattern.compile(" {2}");
        return pattern.matcher(input).replaceAll(" ");
    }
    private static String replaceSpaceBeforeAndAfter(String textWithoutDoubleSpace) {
        return textWithoutDoubleSpace.trim();
    }
    @Test
    public void testNameValidator(){
        String inputText = "TK3G45 fgfg  fgf3234 ";

        boolean isMatch = Pattern.matches(NAME_REGEX_CONTAIN_LOWERCASE_AND_UPPERCASE_AND_NUMBER, inputText);
        if (isMatch){
            String textWithoutDoubleSpace = replaceDoubleSpaces(inputText);
            String textWithoutSpaceBeforeAndAfter = replaceSpaceBeforeAndAfter(textWithoutDoubleSpace);
            boolean isMatch2 = Pattern.matches(NAME_REGEX_WITH_SPACE_IN_BUT_NOT_BEFORE_AND_AFTER_WITHIN_DOUBLE_SPACE, textWithoutSpaceBeforeAndAfter);
            if (isMatch2){
                System.out.println("Test passato " + "("+  textWithoutSpaceBeforeAndAfter + ")");
            }
            else {
                System.out.println("Secondo test non passato " + textWithoutSpaceBeforeAndAfter);
            }

        } else {
            System.out.println("primo test non passato");
        }

    }
}
