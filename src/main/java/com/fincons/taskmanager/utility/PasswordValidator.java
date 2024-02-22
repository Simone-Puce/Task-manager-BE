package com.fincons.taskmanager.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator {
    private static final String PASSWORD_REGEX =
                     "(?=.*[a-z])(?=.*[A-Z])"
                    + "(?=.*[!@#$%^&+=])"
                    + "(?=\\S+$).{8,}$";
    private static final Pattern pattern = Pattern.compile(PASSWORD_REGEX);
    public static boolean isValidPassword(String password) {
        if (password == null) {
            return false;
        }
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
