package com.fincons.taskmanager.enums;

public enum RoleEndpoint {

    USER("ROLE_USER"),
    ADMIN("ROLE_ADMIN");

    private String value;

    RoleEndpoint(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "RoleEndpoint{" +
                "value='" + value + '\'' +
                '}';
    }
}
