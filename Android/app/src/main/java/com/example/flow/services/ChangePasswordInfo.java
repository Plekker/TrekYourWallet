package com.example.flow.services;

public class ChangePasswordInfo {

    private String newPassword;
    private String oldPassword;

    public ChangePasswordInfo(String newPassword, String oldPassword) {
        this.newPassword = newPassword;
        this.oldPassword = oldPassword;
    }
}
