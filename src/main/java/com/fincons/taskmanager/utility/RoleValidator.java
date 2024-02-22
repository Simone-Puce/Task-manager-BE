package com.fincons.taskmanager.utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RoleValidator {
    private static final String ROLE_REGEX = "^ROLE_[a-zA-Z]+$";
    private static final Pattern patter = Pattern.compile(ROLE_REGEX);

    public static boolean isValidRole(String role) {
        if (role == null) {
            return true;
        }
        Matcher matcher = patter.matcher(role);
        return matcher.matches();
    }
}
