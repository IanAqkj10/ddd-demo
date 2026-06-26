package com.example.ddd.application.iam.command;

public class CreateUserCommand {

    private final String username;
    private final String password;

    public CreateUserCommand(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
