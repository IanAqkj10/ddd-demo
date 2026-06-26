package com.example.ddd.application.iam.command;

public class CreateRoleCommand {

    private final String code;
    private final String name;

    public CreateRoleCommand(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
